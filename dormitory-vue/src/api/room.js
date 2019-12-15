import { getRequest, postRequest } from "@/utils/request";
import { uploadFileRequest } from "@/utils/request";
export function getRooms(buildingId, floor) {
    return getRequest("/room/ofFloor", { buildingId, floor });
}

export function uploadBatch(data) {
    return uploadFileRequest("/room/uploadBatch", data);
}

export function saveRoom({ buildingId, number, size }) {
    let data = { buildingId, number, size };
    return postRequest("/room/save", data);
}