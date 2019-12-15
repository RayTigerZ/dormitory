import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "@/assets/iconfont/iconfont.js";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";





Vue.config.productionTip = false;

Vue.use(ElementUI, {
    size: "medium",
    zIndex: 3000
});

var vm = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount("#app");

export default vm;