<template>
	<div>
		<!--弹出框： 批量文件模板下载和上传 -->
		<el-dialog :title="title" :visible.sync="visible">
			<el-form>
				<el-form-item>
					<el-button type="primary" @click="download()" icon="el-icon-download">下载批量导入模板</el-button>
				</el-form-item>
				<el-form-item>
					<el-upload ref="upload" action="" :on-change="handleChange" :auto-upload="false" :file-list="files" :http-request="batchSave" accept=".xlsx">
						<el-button type="primary" icon="el-icon-upload">点击上传</el-button>
						<div slot="tip" class="el-upload__tip">
							只能上传excel文件！！
						</div>
					</el-upload>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="submitUpload">确认上传</el-button>
				</el-form-item>
			</el-form>
		</el-dialog>

		<!-- 弹出框：上传处理的错误信息 -->
		<el-dialog title="错误信息" :visible.sync="errorLayer.visible">

			<template v-for="(msg,index) in errorLayer.msgs">
				<el-alert class="text-warp" :key="index" :title="msg" :closable="false" type="error" show-icon>
				</el-alert>
			</template>

		</el-dialog>
	</div>
</template>

<script>
import { downloadBatchExcel } from '@/api/download'
export default {
	name: 'BatchSave',
	props: {
		title: {
			type: String,
			default: '批量导入'
		},
		code: {
			type: String,
			default: '',
			required: true
		},
		upload: {
			type: Function,
			required: true
		}
	},
	data() {
		return {
			visible: false,
			errorLayer: {
				visible: false,
				msgs: []
			},
			files: []
		}
	},
	methods: {
		download() {
			downloadBatchExcel(this.code)
		},
		open() {
			this.visible = true
		},

		batchSave() {
			let data = new FormData()
			let time = new Date().valueOf()
			// debugger
			data.append('file', this.$refs.upload.uploadFiles[0].raw)
			data.append('time', time)

			this.errorLayer.visible = true
			let ws = this.conWs(time)

			this.upload(data)
				.then(res => {
					this.$message.success(res.data.msg)
					ws.close()
				})
				.catch(err => {
					console.log(err)
				})
		},
		submitUpload() {
			this.$refs.upload.submit()
		},
		handleChange(file, fileList) {
			//debugger
			if (fileList.length > 0) {
				this.files = [fileList[fileList.length - 1]] // 这一步，是 展示最后一次选择的csv文件
			}
		},
		conWs(time) {
			let vm = this
			if ('WebSocket' in window) {
				// 打开一个 web socket
				let ws = new WebSocket('ws://localhost:8090/ws/' + time)
				ws.onopen = function() {
					// Web Socket 已连接上，使用 send() 方法发送数据
					console.log('websocket连接中...')
				}
				ws.onmessage = function(evt) {
					//console.log(evt)
					let target = vm.errorLayer.msgs
					vm.$set(target, target.length, evt.data)
				}

				ws.onclose = function() {
					// 关闭 websocket
					console.log('连接已关闭...')
				}
				return ws
			} else {
				// 浏览器不支持 WebSocket
				vm.$message.error('您的浏览器不支持 WebSocket!')
				return null
			}
		}
	},
	watch: {
		'errorLayer.visible'(newVal) {
			if (newVal == false) {
				this.errorLayer.msgs = []
			}
		},
		visible(newVal) {
			if (newVal == false) {
				this.files = []
			}
		}
	}
}
</script>

<style scoped>
.el-alert {
	margin: 5px 0px;
}

.text-warp {
	white-space: pre-wrap;
}
</style>