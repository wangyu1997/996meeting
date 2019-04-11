export default {
    BaseUrl: "http://219.228.147.134:81",
    Urls: {
        register:'/auth/register',
        login: "/auth/login",
        uploadFile: "/user/upload/doc",
        uploadAvatar: "/public/upload/avatar",
        adminAddTask: "/admin/tasks/add",
        adminUpdateTask: '/admin/tasks/update/',
        adminDelTask: '/admin/tasks/delete/',
        adminUpdateReport: 'user/per_meets/update/',
        adminDelReport: '/user/per_meets/delete/',
        adminAddReport: '/user/per_meets/add',
        adminAddSummary: "/user/summarys/add",
        adminDelSummary: '/user/summarys/delete/',
        adminUpdateSummary: '/user/summarys/update/',
        queryTaskById: '/public/tasks/',
        querySummaryById: '/public/summarys/',
        queryReportById: '/public/per_meets/',
        getFilesByIds: '/public/file/multi/'
    }
}