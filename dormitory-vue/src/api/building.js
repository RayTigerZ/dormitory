import { getRequest, postRequest } from "@/utils/request";

export function getBuildings() {
    return getRequest("/building/list");
}

export function saveBuilding(data) {
    return postRequest("/building/save", data);
}

export function getFloors(buildingId) {
    return getRequest("/building/floors", { buildingId });
}