<template>
	<div class="loading-area">
		<!-- 工具栏 -->
		<div class="toolbar">
			<el-button type="primary" @click="handleOrganizationDialog('add')" icon="el-icon-plus">
				增加组织</el-button>
			<el-button type="primary" @click="openBatch()" icon="el-icon-plus">
				批量导入组织</el-button>
		</div>

		<!--组织 数据表格 -->
		<el-table row-key="id" :data="organizations" :tree-props="{ children: 'children' }" :indent="40" :header-cell-style="{ 'background-color': '#c6c6c6', color: '#42464e' }">
			<el-table-column prop="code" label="编号"></el-table-column>
			<el-table-column prop="name" label="名称"></el-table-column>
			<el-table-column label="操作" align="center" min-width="160">
				<template slot-scope="scope">
					<el-button size="medium" type="primary" icon="el-icon-edit" @click="handleOrganizationDialog('edit', scope.row)" title="编辑组织">
					</el-button>
					<el-button size="medium" type="danger" icon="el-icon-delete" @click="del(scope.row.id)" title="删除组织">
					</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!-- 弹出框：增加(编辑)组织 -->
		<el-dialog :title="organizationDialog.title" :visible.sync="organizationDialog.visible" width="40%">
			<el-form :model="organizationDialog.organization" status-icon ref="organizationForm" :rules="organizationRules" label-width="80px" label-position="right">
				<el-form-item label="组织层次">
					<el-select v-model="organizationDialog.level" :disabled="organizationDialog.disabled" placeholder="请选择">
						<el-option v-for="item in organizationDialog.levels" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="父组织" prop="parentId">
					<el-select v-model.number="organizationDialog.organization.parentId" placeholder="请选择">
						<el-option v-for="item in organizationDialog.parents" :key="item.id" :label="item.name + '(' + item.code + ')'" :value="item.id">
							<span>{{ item.name }}({{ item.code }})</span>
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="组织名称" prop="name">
					<el-input type="text" v-model="organizationDialog.organization.name"></el-input>
				</el-form-item>
				<el-form-item label="组织编码" prop="code">
					<el-input type="text" v-model="organizationDialog.organization.code"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="organizationDialog.visible = false">取消</el-button>
				<el-button type="primary" @click="save()">确定</el-button>
			</div>
		</el-dialog>
		<!-- 弹出框：批量增加班级 -->
		<batch-save ref="batchSave" title="批量导入组织" :code="fileCode" :upload="batchSave"></batch-save>
	</div>
</template>
<script>
import { getAll, save, del, batchSave } from '@/api/organization'
import BatchSave from '@/components/BatchSave'

export default {
	components: { BatchSave },
	data() {
		return {
			organizations: [],
			organizationDialog: {
				disabled: false,
				level: 1,
				levels: [
					{ id: 1, name: '学院' },
					{ id: 2, name: '专业' },
					{ id: 3, name: '班级' }
				],
				parents: [],
				visible: false,
				title: '',
				organization: {
					id: '',
					code: '',
					name: '',
					parentId: ''
				}
			},
			organizationRules: {
				code: [
					{
						required: true,
						pattern: '^[0-9]{3}$',
						message: '请输入三位数的组织编码',
						tigger: 'blur'
					}
				],
				name: [
					{
						required: true,
						message: '请输入组织名称',
						tigger: 'blur'
					}
				],
				parentId: [
					{
						required: false,
						type: 'integer',
						message: '请输入父组织',
						tigger: 'blur'
					}
				]
			},
			fileCode: 'organizationBatch',

			levelMap: {
				3: 1,
				5: 2,
				10: 3
			}
		}
	},
	methods: {
		getAll() {
			getAll()
				.then(res => {
					this.organizations = res.data.data
				})
				.catch(err => {
					console.log(err)
				})
		},
		handleOrganizationDialog(type, data) {
			let dialog = this.organizationDialog
			if (type == 'edit') {
				dialog.disabled = true
				dialog.level = this.levelMap[data.code.length]
				dialog.title = '编辑组织'
				this.$nextTick(function() {
					dialog.organization = { ...data }
				})
			} else if (type == 'add') {
				dialog.disabled = false
				dialog.title = '增加组织'
			}

			dialog.visible = true
		},
		save() {
			this.$refs['organizationForm'].validate(valid => {
				if (valid) {
					let dialog = this.organizationDialog
					save({ ...dialog.organization })
						.then(res => {
							let code = res.data.code
							if (code == 200) {
								this.$message.success(res.data.msg)
								dialog.visible = false
								this.getAll()
							}
						})
						.catch(err => {
							console.log(err)
						})
				}
			})
		},
		del(id) {
			this.$confirm('此操作将永久删除该组织, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				del(id)
					.then(res => {
						if (res.data.code == 200) {
							this.$message.success(res.data.msg)
							this.getAll()
						}
					})
					.catch(err => console.log(err))
			})
		},
		batchSave(data) {
			return batchSave(data)
		},
		openBatch() {
			this.$refs['batchSave'].open()
		}
	},

	created() {
		this.getAll()
	},
	watch: {
		'organizationDialog.visible': {
			handler(newVal) {
				if (newVal == false) {
					this.$refs['organizationForm'].resetFields()
					this.organizationDialog.organization.id = ''
					this.organizationDialog.level = 1
					this.organizationDialog.parents = []
				}
			}
		},
		'organizationDialog.level': {
			handler(newVal) {
				let codeRules = this.organizationRules.code
				let parentRules = this.organizationRules.parentId
				this.organizationDialog.organization.parentId = ''
				if (newVal == 1) {
					this.organizationDialog.parents = []
					parentRules[0] = {
						required: false,
						type: '',
						message: '',
						tigger: ''
					}
					codeRules[0].pattern = '^[0-9]{3}$'
					codeRules[0].message = '请输入三位数的组织编码'
				} else if (newVal == 2) {
					parentRules[0] = {
						required: true,
						type: 'integer',
						message: '请输入父组织',
						tigger: 'blur'
					}
					codeRules[0].pattern = '^[0-9]{5}$'
					codeRules[0].message = '请输入五位数的组织编码'
				} else if (newVal == 3) {
					parentRules[0] = {
						required: true,
						type: 'integer',
						message: '请输入父组织',
						tigger: 'blur'
					}
					codeRules[0].pattern = '^[0-9]{10}$'
					codeRules[0].message = '请输入十位数的组织编码'
				}

				if (newVal >= 2) {
					getAll(newVal - 1).then(res => {
						this.organizationDialog.parents = res.data.data
					})
				}
			},
			immediate: true
		}
	}
}
</script>

<style scoped>
.toolbar {
	margin-bottom: 10px;
}
</style>
