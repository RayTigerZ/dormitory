<template>
	<div>
		<el-collapse accordion>
			<el-collapse-item v-for="item in questionnaires" :key="item.id">
				<template slot="title">

					<h3 class="questionnaire-title" v-text="item.title"></h3>
					<div class="oprate">
						<el-button type="primary">编辑</el-button>
					</div>

				</template>
				<div class="question" v-for="(question,index) in item.questions" :key="question.id">
					<div>{{index+1}}.{{question.title}}</div>
					<div class="options">
						<span class="option" v-for="(option,index) in question.options" :key="option.id">
							{{String.fromCharCode(index + 65)}}.{{option.option}}
						</span>
					</div>
				</div>

			</el-collapse-item>
		</el-collapse>
	</div>

</template>

<script>
import { getQuestionnaires } from '@/api/questionnaire.js'
export default {
	data() {
		return {
			questionnaires: []
		}
	},
	methods: {
		getQuestionnaires() {
			getQuestionnaires()
				.then(res => {
					if (res.data.code == 200) {
						this.questionnaires = res.data.data
					}
				})
				.catch(err => {
					console.log(err)
				})
		}
	},
	created() {
		this.getQuestionnaires()
	}
}
</script>

<style scoped>
.questionnaire-title {
	padding: 0px 20px;
	font-size: 16px;
	font-weight: 500;
	display: block;
}
.el-collapse-item .el-collapse-item__header {
	justify-content: space-between;
}
.oprate {
	float: right;
}
</style>