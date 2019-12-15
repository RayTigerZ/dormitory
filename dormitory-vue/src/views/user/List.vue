<template>
	<div>
		<div class="toolbar">
			<el-form :inline="true" :model="search">
				<el-form-item>
					<el-input v-model="search.account" placeholder="输入查询的帐号" clearable></el-input>
				</el-form-item>
				<el-form-item>
					<el-select v-model="search.roleId" clearable placeholder="请选择角色">
						<el-option v-for="item in roles" :key="item.id" :label="item.nameZh" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-cascader v-model="classId" :options="classList" placeholder="请选择班级" collapse-tags filterable clearable :show-all-levels="false" :props="{
							label: 'name',
							children: 'children',
							value: 'id',
							expandTrigger: 'hover',
							multiple: true,
							leaf: 'children'
						}">
					</el-cascader>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" @click="getUsers()">搜索</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-plus" @click="handleUserDialog('add')">增加用户</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="openBatch" icon="el-icon-document">批量导入</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-table :data="users" border highlight-current-row :header-cell-style="{ 'background-color': '#c6c6c6', color: '#42464e' }">
			<el-table-column prop="account" label="账户" align="center" width="140">
			</el-table-column>
			<el-table-column prop="name" label="姓名" align="center" width="120"> </el-table-column>
			<el-table-column prop="phone" label="手机号码" align="center" width="120">
			</el-table-column>
			<el-table-column prop="email" label="邮箱" align="center"> </el-table-column>
			<el-table-column prop="sex" label="性别" align="center" width="70"> </el-table-column>
			<el-table-column prop="college" label="学院" align="center"> </el-table-column>
			<el-table-column prop="cla" label="班级" align="center"> </el-table-column>
			<el-table-column prop="roomId" label="宿舍号" align="center" width="80">
			</el-table-column>

			<el-table-column label="操作" align="center" min-width="160">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isUsable" active-color="#13ce66" inactive-color="#ff4949" title="启用/禁用帐号" @click.native="enable(scope.row.id,scope.row.isUsable)" disabled>
					</el-switch>
					<el-button size="medium" type="primary" icon="el-icon-edit" @click="handleUserDialog('edit', scope.row)" title="编辑">
					</el-button>
					<el-button size="medium" type="danger" icon="el-icon-refresh" @click="resetPsw(scope.row.id)" title="重置密码">
					</el-button>

				</template>
			</el-table-column>
		</el-table>
		<el-pagination hide-on-single-page :current-page.sync="search.pageNum" :page-sizes="[10, 20, 50, 100]" :page-size.sync="search.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
		</el-pagination>

		<!-- 弹出框：增加/更新用户 -->
		<el-dialog :title="userDialog.title" :visible.sync="userDialog.visible" width="40%" top="4vh">
			<el-form ref="userForm" :model="userDialog.user" label-position="right" label-width="80px" :rules="userRules">
				<el-form-item label="姓名" prop="name">
					<el-input type="text" v-model="userDialog.user.name"></el-input>
				</el-form-item>
				<el-form-item label="学号" prop="account">
					<el-input type="text" v-model="userDialog.user.account"></el-input>
				</el-form-item>
				<el-form-item label="性别" prop="sex">
					<el-radio v-model="userDialog.user.sex" label="男">男</el-radio>
					<el-radio v-model="userDialog.user.sex" label="女">女</el-radio>
				</el-form-item>
				<el-form-item label="角色" prop="roleId">
					<el-checkbox-group v-model="userDialog.user.roleId">
						<template v-for="role in roles">
							<el-checkbox :key="role.id" :label="role.id">{{
								role.nameZh
							}}</el-checkbox>
						</template>
					</el-checkbox-group>
				</el-form-item>
				<el-form-item label="宿舍号" prop="roomId">
					<el-input type="text" v-model="userDialog.user.roomId"></el-input>
				</el-form-item>
				<el-form-item label="班级" prop="classId">
					<el-cascader v-model="userDialog.user.classId" :options="classList" :props="{ expandTrigger: 'hover', value: 'id', label: 'name' }" :show-all-levels="false" clearable></el-cascader>
				</el-form-item>

				<el-form-item label="邮箱" prop="email">
					<el-input type="text" v-model="userDialog.user.email"></el-input>
				</el-form-item>
				<el-form-item label="联系电话" prop="phone">
					<el-input type="text" v-model="userDialog.user.phone"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="userDialog.visible = false">取消</el-button>
				<el-button type="primary" @click="saveUser()">确定</el-button>
			</div>
		</el-dialog>
		<!-- 弹出框：批量导入用户 -->
		<batch-save ref="batchSave" title="批量导入用户" :code="fileCode" :upload="batchSave"></batch-save>
	</div>
</template>

<script>
import BatchSave from '@/components/BatchSave'
import { getRoles } from '@/api/role'
import { batchSave, getPageOfUser, saveUser, enable } from '@/api/user'
import { getAll } from '@/api/organization'

export default {
	components: { BatchSave },
	data() {
		return {
			users: [],
			total: 0,
			search: {
				pageNum: 1,
				pageSize: 10,
				account: '',
				roleId: '',
				classId: []
			},
			classId: [],

			roles: [],
			classList: [],

			userDialog: {
				title: '',
				visible: false,
				user: {
					id: '',
					name: '',
					account: '',
					sex: '',

					roleId: [],
					classId: '',
					roomId: '',
					phone: '',
					email: ''
				}
			},
			fileCode: 'userBatch',
			userRules: {
				name: [
					{
						required: true,
						message: '请输入用户真实姓名',
						tigger: 'blur'
					}
				],
				account: [
					{
						required: true,
						min: 8,
						max: 12,
						message: '请输入8-12位的用户帐号',
						tigger: 'blur'
					}
				],
				sex: [
					{
						required: true,
						message: '请选择用户性别',
						tigger: 'blur'
					}
				],
				roleId: [
					{
						required: true,
						min: 1,
						type: 'array',
						message: '请选择用户角色',
						tigger: 'blur'
					}
				],
				classId: [
					{
						required: false
					}
				],
				roomId: [
					{
						required: false,
						pattern: '^[0-9]{6}$',
						message: '请输入正确格式的宿舍号',
						tigger: 'blur'
					}
				],
				phone: [
					{
						required: false,
						pattern: '^1[3456789][0-9]{9}$',
						message: '请输入正确格式的手机号码',
						tigger: 'blur'
					}
				],
				email: [
					{
						required: false,
						type: 'email',
						message: '请输入正确格式的邮箱号码',
						tigger: 'blur'
					}
				]
			}
		}
	},
	methods: {
		getUsers() {
			this.search.classId = []
			let classId = this.search.classId

			this.classId.forEach(element => {
				this.$set(classId, classId.length, element[2])
			})

			getPageOfUser({ ...this.search })
				.then(res => {
					if (res.data.code == 200) {
						this.users = res.data.data.records
						this.total = res.data.data.total
					}
				})
				.catch(err => {
					console.log(err)
				})
		},
		getRoles() {
			getRoles({
				pageNum: 1,
				pageSize: 100
			}).then(res => {
				if (res.data.code == 200) {
					this.roles = res.data.data.records
				}
			})
		},
		getClassList() {
			getAll().then(res => {
				this.classList = res.data.data
			})
		},
		batchSave(data) {
			return batchSave(data)
		},
		openBatch() {
			this.$refs['batchSave'].open()
		},
		handleUserDialog(type, user) {
			let dialog = this.userDialog
			if (type == 'edit') {
				dialog.title = '编辑用户'
				console.log(user)
				debugger
				dialog.user = { ...user }
				let roleId = []
				user.roles.forEach(element => {
					roleId.push(element.id)
				})
				this.$set(dialog.user, 'roleId', roleId)
				this.$delete(dialog.user, 'roles')
				console.log(dialog.user)
			} else if (type == 'add') {
				dialog.title = '增加用户'
			}
			dialog.visible = true
		},
		saveUser() {
			this.$refs['userForm'].validate(valid => {
				if (valid) {
					saveUser(this.userDialog.user)
						.then(res => {
							if (res.data.code == 200) {
								this.$message.success(res.data.msg)
								this.getUsers()
							} else {
								this.$message.error(res.data.msg)
							}
						})
						.catch(err => {
							console.error(err)
						})
				}
			})
		},
		enable(id, status) {
			let str = status ? '禁用' : '启用'
			this.$confirm(`此操作将${str}该帐号, 是否继续?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(() => {
					enable(id)
						.then(res => {
							if (res.data.msg) {
								this.$essage.success(res.data.msg)
							}
						})
						.catch(err => {
							this.$message.error(err)
						})
				})
				.catch(() => {
					this.$message.info('已取消操作')
				})
		},
		resetPsw(id) {
			console.log(id)
		}
	},
	watch: {
		'search.pageNum'() {
			this.getUsers()
		},
		'search.pageSize'() {
			this.search.pageNum = 1
			this.getUsers()
		},
		'userDialog.visible'(newVal) {
			if (newVal == false) {
				this.$refs['userForm'].resetFields()
				this.userDialog.user.id = ''
			}
		}
	},
	created() {
		this.getUsers()
		this.getRoles()
		this.getClassList()
	}
}
</script>

<style scoped>
.toolbar {
	margin-bottom: 0px;
}
.toolbar .el-form-item {
	margin-bottom: 10px;
}
.el-cascader--medium {
	width: 390px;
}
.el-switch {
	margin-right: 10px;
}
</style>
