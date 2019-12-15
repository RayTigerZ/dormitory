<template>
	<div>
		<el-table :data="menu"  row-key="id" :header-cell-style="headerStyle" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" :indent="32">
			<el-table-column prop="name" label="名称" min-width="200"> </el-table-column>
			<el-table-column prop="path" align="center" label="路径" width="180"> </el-table-column>
			<el-table-column prop="component" align="center" width="180" label="组件"> </el-table-column>
			<el-table-column prop="type" align="center" label="类型" width="140" :formatter="formatType">
			</el-table-column>
			<el-table-column align="center" min-width="300" label="操作">
				<template slot-scope="scope">
					<el-button size="medium" type="primary" icon="el-icon-edit" @click="handleDialog('edit', scope.row)" title="编辑节点">
					</el-button>

					<el-button size="medium" type="primary" icon="el-icon-plus" title="增加子节点" :disabled="scope.row.type >= 2" @click="handleDialog('add', scope.row)">
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-dialog :title="dialogTitle" :visible.sync="menuDialogVisible">
			<el-form :model="dialogMenu">
				<el-form-item label="英文名称">
					<el-input v-model="dialogMenu.name" autocomplete="off"></el-input>
				</el-form-item>

				<el-form-item label="路由地址">
					<el-input v-model="dialogMenu.path"> </el-input>
				</el-form-item>
				<el-form-item label="组件">
					<el-input v-model="dialogMenu.component"> </el-input>
				</el-form-item>
				<el-form-item label="类型">
					<el-select v-model="dialogMenu.type" placeholder="请选择">
						<el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value" :disabled="item.disabled">
						</el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="menuDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="save()">确定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { getRequest, postRequest } from '../../utils/request'
import { Message } from 'element-ui'

export default {
	data() {
		return {
			menu: [],
			menuDialogVisible: false,
			dialogTitle: '',
			dialogMenu: {},
			types: [
				{ value: 0, label: '根菜单', disabled: true },
				{ value: 1, label: '菜单', disabled: false },
				{ value: 2, label: '页面', disabled: false },
				{ value: 3, label: '按钮', disabled: false }
			],
			headerStyle: { 'background-color': '#c6c6c6', color: '#42464e' }
		}
	},
	methods: {
		formatType(rol, col, cellValue) {
			if (cellValue == 0) return '根菜单'
			else if (cellValue == 1) return '菜单'
			else if (cellValue == 2) return '页面'
			else if (cellValue == 3) return '按钮'
			else return ''
		},
		getMenuTree() {
			getRequest('/permission/all')
				.then(res => {
					if (res.data.code == 200) {
						this.menu = [res.data.data]
					}
				})
				.catch(err => {
					Message.error(err)
				})
		},
		updateMenu() {
			postRequest()
		},
		handleDialog(type, node) {
			this.menuDialogVisible = true
			if (type == 'edit') {
				this.dialogTitle = '编辑节点'
				this.dialogMenu = node
			} else if (type == 'add') {
				this.dialogTitle = '增加子节点'
				this.dialogMenu = {
					id: '',
					url: '',
					path: '',
					component: '',
					name: '',
					iconClass: '',
					type: '',
					parentId: node.id,
					keepAlive: true,
					requireAuth: true
				}
			}
		},
		save() {
			console.log(this.dialogMenu)
			this.dialogMenu.children = '[]'
			this.dialogMenu.roles = '[]'
			delete this.dialogMenu['children']
			delete this.dialogMenu['roles']
			postRequest('/permission/save', this.dialogMenu)
				.then(res => {
					if (res.data.code == 200) {
						Message.success(res.data.msg)
					} else {
						Message.error(res.data.msg)
					}
				})
				.catch(err => {
					Message.error(err)
				})
		}
	},
	created() {
		this.getMenuTree()
	}
}
</script>

<style scoped>
.tree-node {
	padding: 20px;
}
.tree-node-data {
	padding-right: 45px;
}
.tree-node-op {
}
</style>
