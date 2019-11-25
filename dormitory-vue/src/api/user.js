import { uploadFileRequest} from "@/utils/request";

export function uploadBatch(data) {
    return uploadFileRequest ("/user/uploadBatch",data);
}