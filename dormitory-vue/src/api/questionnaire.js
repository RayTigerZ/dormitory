import { getRequest } from '@/utils/request';

export function getQuestionnaires() {
	return getRequest('/questionnaires');
}
