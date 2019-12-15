<template>
	<div class="icon-div">

		<svg class="icon" aria-hidden="true" @click="click">
			<use :xlink:href="href"></use>
		</svg>
	</div>
</template>

<script>
import screenfull from 'screenfull'

export default {
	name: 'Screenfull',
	data() {
		return {
			isFullscreen: false
		}
	},
	mounted() {
		this.init()
	},
	beforeDestroy() {
		this.destroy()
	},
	computed: {
		href() {
			return this.isFullscreen ? '#icon-tuichuquanping' : '#icon-quanping'
		}
	},
	methods: {
		click() {
			debugger
			console.log(screenfull)
			if (screenfull.isEnabled) {
				screenfull.toggle()
			} else {
				this.$message.warning('您当前的浏览器不支持全屏功能！')
			}
		},
		change() {
			this.isFullscreen = screenfull.isFullscreen
		},
		init() {
			if (screenfull.isEnabled) {
				screenfull.on('change', this.change)
			}
		},
		destroy() {
			if (screenfull.isEnabled) {
				screenfull.off('change', this.change)
			}
		}
	}
}
</script>

<style scoped>
/* .screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
} */
.icon-div {
	padding: 0px 15px;
}
.icon {
	font-size: 24px;
}
</style>
