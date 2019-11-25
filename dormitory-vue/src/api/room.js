import { getRequest } from "@/utils/request";
import { uploadFileRequest } from "@/utils/request";
export function getRooms(buildingId, floor) {
    return getRequest("/room/ofFloor", { buildingId, floor });
}

export function uploadBatch(data) {
    return uploadFileRequest("/room/uploadBatch", data);
}