const menuList = [
    {
        title: '首页',
        key: '/home'
    },
    {
        title: '个人操作',
        key: '/user',
        children: [
            {
                title: '我的汇报',
                key: '/user/reports',
            },
            {
                title: '我的任务',
                key: '/user/tasks',
            },
            {
                title: '我的总结',
                key: '/user/summaries',
            },
            {
                title: '发布汇报',
                key: '/user/addReport',
            },
            {
                title: '发布总结',
                key: '/user/addSummary',
            },
            {
                title: '个人信息',
                key: '/user/info',
            }
        ]
    },
    {
        title: '管理员操作',
        key: '/admin',
        children: [
            {
                title: '任务管理',
                key: '/admin/tasks',
            },
            {
                title: '个人汇报管理',
                key: '/admin/reports',
            },
            {
                title: '组会总结管理',
                key: '/admin/reports',
            },
            {
                title: '用户信息管理',
                key: '/admin/reports',
            }
        ]
    },
    {
        title: '超级管理员操作',
        key: '/sysuser',
        children: [
            {
                title: '权限管理',
                key: '/sysuser/permission',
            }
        ]
    }
]
export default menuList