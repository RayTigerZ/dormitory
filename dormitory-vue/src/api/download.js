import { postRequest } from "@/utils/request";
import { fileDownload } from "@/utils/utils";
import { Message } from "element-ui";
export function downloadBatchExcel(code) {
    postRequest("/download/batchExcel", { code }, "arraybuffer")
        .then(res => {
            fileDownload(res.data, res.headers["filename"]);
        })
        .catch(err => {
            Message.error(err);
        });
}