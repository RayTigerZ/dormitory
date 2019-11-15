import {postRequest} from '../utils/request';

export const login=postRequest("/user/login",params);