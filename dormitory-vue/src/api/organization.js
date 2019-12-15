import {
    postRequest,
    getRequest,
    uploadFileRequest
} from "@/utils/request";

export function getAll(level) {
    let data = {
        level
    };
    return getRequest("/organization/all", data);
}

export function save({
    id,
    code,
    name,
    parentId
}) {
    if (parentId == null) {
        parentId = '';
    }
    let data = {
        id,
        code,
        name,
        parentId
    };
    return postRequest("/organization/save", data);
}

export function batchSave(data) {
    return uploadFileRequest('/organization/batchSave', data)
}

export function del(id) {
    let data = {
        id
    }
    return postRequest("/organization/delete", data);
}