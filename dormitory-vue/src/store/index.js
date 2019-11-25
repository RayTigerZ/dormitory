import Vue from "vue";
import Vuex from "vuex";
import { login, logout } from "@/api/login";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        token: sessionStorage.getItem("dormitory-token") || "",
        menu: "",
        userName: sessionStorage.getItem("dormitory-userName") || ""
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
            sessionStorage.setItem("dormitory-token", token);
        },
        setUserName(state, userName) {
            state.userName = userName;
            sessionStorage.setItem("dormitory-userName", userName);
        },
        setMenu(state, menu) {
            state.menu = menu;
        }
    },
    actions: {
        // 用户名登录
        login({ commit }, account, password) {
            return new Promise((resolve, reject) => {
                login(account, password)
                    .then(res => {
                        if (res.data.code == 200) {
                            commit("setToken", res.data.data.token);

                            commit("setUserName", res.data.data.userName);
                        }

                        resolve(res.data);
                    })
                    .catch(err => {
                        reject(err);
                    });
            });
        },
        logout({ commit }) {
            return new Promise((resolve, reject) => {
                logout()
                    .then(() => {
                        commit("setToken", "");

                        commit("setUserName", "");
                        resolve();
                        reject();
                    })
                    .catch(error => {
                        reject(error);
                    });
            });
        }
    }
});