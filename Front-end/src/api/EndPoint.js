// Public API
export const Authen = {
    signUp: "/authen/signup",
    signIn: "/authen/signin",
}

export const PublicAPI = {
    getBikePagination: "/public/bike/get",
    getBikeDetail: "/public/bike/get?bikeId="
}

// Private API
export const Role = {
    create: "/admin/role/create/",
}

export const BikeAPI = {
    create: "/admin/bike/create",
    update: "/admin/bike/update/",
    delete: "/admin/bike/delete/",
    deteteImage: "/admin/bike/image/delete/",
    getBikePagination: "/admin/bike/get",
    getById: "/admin/bike/get?bikeId="
};

export const CategoryAPI = {
    create: "/admin/bike-category/create",
    update: "/admin/bike-category/update/",
    delete: "/admin/bike-category/delete/",
    getPagination: "/admin/bike-category/get",
    getById: "/admin/bike-category/get?id="
}

export const ColorAPI = {
    create: "/admin/bike-color/create",
    update: "/admin/bike-color/update/",
    delete: "/admin/bike-color/delete/",
    getPagination: "/admin/bike-color/get",
    getById: "/admin/bike-color/get?id="
}

export const ManufacturerAPI = {
    create: "/admin/bike-manufacturer/create",
    update: "/admin/bike-manufacturer/update/",
    delete: "/admin/bike-manufacturer/delete/",
    getPagination: "/admin/bike-manufacturer/get",
    getById: "/admin/bike-manufacturer/get?id="
}

export const OrderAPI = {
    cartAddBike: "/admin/order/cart/add-bike",
    cartGetByUsername: "/admin/order/cart/get",
    cartGetBikeNumber: "/admin/order/cart/get/bike-number",
    cartDeleteBike: "/admin/order/cart/delete-bike/",
    cartSave: "/admin/order/cart/save",
    cartCalculateCost: "/admin/order/cart/calculate-hiring-cost",
    getPagination: "/admin/order/get",
    getById: "/admin/order/get?id=",
    saveOrder: "/admin/order/save",
    cancelOrder: "/admin/order/cancel"
}

export const MaintainAPI = {
    create: "/admin/maintain/create",
    update: "/admin/maintain/update",
    delete: "/admin/maintain/delete/",
    getPagination: "/admin/maintain/get",
    getById: "/admin/maintain/get?id="
}

export const DashboardAPI = {
    getByDateFromTo: "/admin/dashboard/getByDateFromTo",
    getByYear: "/admin/dashboard/getByYear"
}

// Fire Base
export const Firebase_URL = "https://firebasestorage.googleapis.com/v0/b/bike-hiring-management-d7a01.appspot.com/o/bike-image%2F"