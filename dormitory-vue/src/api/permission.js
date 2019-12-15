import {
    getRequest
} from '@/utils/request'

export function getMenu() {
    return getRequest("/permission/menu");
}