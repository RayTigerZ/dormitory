<template>
	<el-form label-width="90px" status-icon label-position="right" :rules="pswRules" ref="pswForm" :model="formData">
		<el-form-item label="原密码" prop="oldPsw">
			<el-input type="password" v-model="formData.oldPsw" auto-complete="off"></el-input>
		</el-form-item>
		<el-form-item label="新密码" prop="newPsw">
			<el-input type="password" v-model="formData.newPsw" auto-complete="off"></el-input>
		</el-form-item>
		<el-form-item label="确认密码" prop="confirmPsw">
			<el-input type="password" v-model="formData.confirmPsw" auto-complete="off"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="editPsw">确认修改</el-button>
		</el-form-item>
	</el-form>
</template>
<script>
import { editPsw } from '@/api/login'
export default {
	data() {
		var checkEqual = (rule, value, callback) => {
			if (value != this.formData.newPsw) {
				callback(new Error('两次输入的密码不一致'))
			} else {
				callback()
			}
		}
		return {
			formData: {
				oldPsw: '',
				newPsw: '',
				confirmPsw: ''
			},

			pswRules: {
				oldPsw: [
					{
						required: true,
						message: '请输入你的原密码',
						trigger: 'blur'
					}
				],
				newPsw: [
					{
						required: true,
						message: '请输入你的新密码',
						trigger: 'blur'
					},
					{
						min: 7,
						max: 16,
						message: '密码长度必须在7-16位',
						trigger: 'blur'
					}
				],
				confirmPsw: [
					{ required: true, message: '请确认密码', trigger: 'blur' },
					{ validator: checkEqual, trigger: 'blur' }
				]
			}
		}
	},
	methods: {
		editPsw() {
			this.$refs['pswForm'].validate(valid => {
				if (valid) {
					editPsw(this.formData.oldPsw, this.formData.newPsw)
						.then(res => {
							if (res.data.code == 200) {
								this.$message.success(res.data.msg)
							} else {
								this.$message.error(res.data.msg)
							}
						})
						.catch(err => {
							console.log(err)
						})
				}
			})
		}
	}
}
</script>

<style scoped>
.el-form {
	width: 40%;
}
</style>
