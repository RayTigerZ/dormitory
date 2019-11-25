<template>
	<el-container class="manage-container">
		<el-aside :width="menuHide == true ? '60px' : '200px'">
			<el-scrollbar>
				<el-menu
					background-color="#223041"
					text-color="#BFCBD9"
					:collapse="menuHide"
					router
					:default-active="this.$route.path"
					collapse-transition
				>
					<template v-for="(item, index) in menu.children">
						<template v-if="item.type == 2">
							<el-menu-item :index="item.path" :key="item.path">
								<i :class="item.iconClass"></i
								><span>{{ item.meta.name }}</span></el-menu-item
							>
						</template>
						<template v-else>
							<el-submenu :key="index" :index="index + ''">
								<template slot="title">
									<i :class="item.iconClass"></i>
									<span>{{ item.meta.name }}</span>
								</template>
								<el-menu-item
									v-for="child in item.children"
									:index="child.path"
									:key="child.path"
									>{{ child.meta.name }}</el-menu-item
								>
							</el-submenu>
						</template>
					</template>
				</el-menu>
			</el-scrollbar>
		</el-aside>

		<el-container>
			<el-header>
				<div class="header-left">
					<div class="hide-div" @click="menuHide = !menuHide">
						<i :class="hideIcon" />
					</div>

					<el-breadcrumb separator="/">
						<el-breadcrumb-item :to="{ path: '/index' }"
							>宿舍管理系统</el-breadcrumb-item
						>
					</el-breadcrumb>
				</div>
				<div class="header-right">
					<el-dropdown>
						<span>
							{{ userName }}
							<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item>个人中心</el-dropdown-item>
							<el-dropdown-item>设置</el-dropdown-item>
							<el-dropdown-item divided @click.native="logout()"
								>注销</el-dropdown-item
							>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
			</el-header>

			<el-main>
				<el-scrollbar>
					<router-view></router-view>
				</el-scrollbar>
			</el-main>
		</el-container>
	</el-container>
</template>
<script>
export default {
	data() {
		return {
			isDot: false,
			user: {
				name: ''
			},
			menuHide: false,

			routers: {}
		}
	},
	methods: {
		logout() {
			this.$confirm('确认退出登录吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$store.dispatch('logout').then(() => {
					location.reload()
				})
			})
		}
	},

	computed: {
		hideIcon() {
			return this.menuHide ? 'el-icon-s-unfold' : 'el-icon-s-fold'
		},
		userName() {
			return this.$store.state.userName
		},
		menu() {
			return this.$store.state.menu
		}
	}
}
</script>
<style scoped>
.el-aside {
	background-color: #223041;
}
.manage-container {
	min-width: 1200px;
}
.el-menu:not(.el-menu--collapse) {
	width: 200px;
}
.el-menu--collapse {
	width: 60px;
}
.el-header {
	padding: 0px;
	display: flex;
	justify-content: space-between;
}
.header-left {
	display: flex;
	height: 50px;
	line-height: 50px;
}

.hide-div {
	text-align: center;
	width: 50px;
	font-size: 25px;
}
.header-right {
	height: 50px;
	line-height: 50px;
	padding-right: 30px;
}

.el-container,
.el-aside,
.el-scrollbar,
.el-main,
.el-scrollbar__view,
.el-menu {
	height: 100%;
}
.el-menu {
	border: none;
}

.el-scrollbar__view {
	position: absolute;
	top: 0px;
	bottom: 0px;
}

.el-scrollbar::-webkit-scrollbar {
	display: none;
}

.el-menu-item {
	height: 56px;
	line-height: 56px;
	font-size: 14px;
}

.el-breadcrumb {
	font-size: 14px;
	line-height: 50px;
}

.el-scrollbar__wrap {
	overflow-x: hidden;
}

.el-main {
	background-color: #fafafa;
	padding: 10px 10px 0px 10px;
}
</style>
