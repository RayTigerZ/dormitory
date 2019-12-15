import { downloadRequest } from '@/utils/request';
import { fileDownload } from '@/utils/utils';

export function downloadBatchExcel(code) {
	downloadRequest('/download/batchExcel', { code })
		.then(res => {
			fileDownload(res.data, res.headers['filename']);
		})
		.catch(err => {
			console.log(err);
		});
}
