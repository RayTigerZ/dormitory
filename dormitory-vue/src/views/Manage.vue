<template>
	<el-container class="manage-container">
		<!-- 侧边栏 -->
		<el-aside width="auto">
			<!-- logo -->

			<div class="logo">
				<svg class="icon logo-icon" aria-hidden="true">
					<use xlink:href="#icon-susheguanlixitong"></use>
				</svg>
				<!-- <transition name="el-zoom-in-center"> -->
				<span class="logo-title" :class="menuHide? 'bb':'aa'">
					&nbsp;&nbsp;Dormitory
				</span>
				<!-- </transition> -->
			</div>

			<el-scrollbar>
				<el-menu background-color="#304156" text-color="#BFCBD9" :collapse="menuHide" router :default-active="this.$route.path" collapse-transition>
					<template v-for="(item, index) in menu.children">
						<template v-if="item.type == 2">
							<el-menu-item :index="item.path" :key="item.path">
								<i :class="item.iconClass"></i><span>{{ item.meta.name }}</span></el-menu-item>
						</template>
						<template v-else>
							<el-submenu :key="index" :index="index + ''" :popper-class="'popper'">
								<template slot="title">
									<i :class="item.iconClass"></i>
									<span>{{ item.meta.name }}</span>
								</template>
								<el-menu-item v-for="child in item.children" :index="child.path" :key="child.path">{{ child.meta.name }}</el-menu-item>
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
						<el-breadcrumb-item class="system-title" :to="{ path: '/index' }">宿舍管理系统</el-breadcrumb-item>
					</el-breadcrumb>
				</div>
				<div class="header-right">
					<screenfull class="left-float"></screenfull>
					<el-dropdown class="left-float">
						<span class="username">
							{{ userName }}
							<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item>个人中心</el-dropdown-item>
							<el-dropdown-item>设置</el-dropdown-item>
							<el-dropdown-item divided @click.native="logout()">注销</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
				<!-- <tag-view></tag-view> -->

			</el-header>

			<el-main>
				<transition name="fade-transform" mode="out-in">
					<el-scrollbar class="content-scrollbar">
						<el-backtop target=".el-scrollbar__wrap"></el-backtop>
						<router-view></router-view>

					</el-scrollbar>
				</transition>
			</el-main>
		</el-container>
	</el-container>
</template>
<script>
// import TagView from '@/components/TagView'
import Screenfull from '@/components/Screenfull'
export default {
	components: {
		// TagView
		Screenfull
	},
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
	background-color: #304156;
}
.logo {
	height: auto;
	position: fixed;
	background-color: #2f4156;
	top: 0px;
	left: 0px;
	z-index: 3000;
	font-weight: bold;
	box-shadow: 0 1px 0 0 rgba(0, 0, 0, 0.05);
}

.logo-icon {
	width: 3.75em;
	height: 3.75em;
}
.logo-title {
	/* transition: width 2s; */
	display: inline-block;
	font-size: 18px;
	font-style: italic;
	color: #ffffff;
	width: 140px;
}
.el-aside .el-scrollbar {
	top: 62px;
}
.el-scrollbar__wrap {
	overflow-x: hidden;
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
	font-size: 32px;
}
.header-right {
	height: 50px;
	line-height: 50px;
	padding-right: 30px;
}

.el-container,
.el-aside,
.el-main,
.el-menu {
	height: 100%;
}
.el-menu {
	border: none;
}
.bb {
	width: 0px;
}
.aa {
	width: 140px;
	transition: width 2s;
}
.el-menu-item {
	height: 50px;
	line-height: 50px;
	font-size: 14px;
	font-family: Helvetica Neue, Helvetica, PingFang SC, Tahoma, Arial,
		sans-serif;
}

.el-submenu .el-menu .el-menu-item {
	background-color: #1f2d3d !important;
}

.el-breadcrumb {
	font-size: 15px;
	line-height: 50px;
}

.system-title {
	font-size: 16px;
	font-weight: 700;
}

.el-main {
	background-color: #fafafa;
	padding: 10px 10px 0px 10px;
}

.left-float {
	float: left;
}
.username {
	font-size: 15px;
}
</style>
