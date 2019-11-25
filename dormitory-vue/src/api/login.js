import { postRequest } from "../utils/request";
import md5 from "js-md5";

export function login(account, password) {
    let data = {
        account: account,
        password: md5(password)
    };

    return postRequest("/user/login", data);
}

export function logout() {
    return postRequest("/user/logout");
}

export function editPsw(oldVal, newVal) {
    let data = {
        oldPsw: md5(oldVal),
        newPsw: md5(newVal)
    };

    return postRequest("/user/editPsw", data);
}