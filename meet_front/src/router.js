import React, {Component} from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import PrivateRoute from './components/PrivateRoute'
import App from './App'
import Login from './pages/login'
import Admin from './admin'
import AdminTask from './pages/admin/tasks'
import AdminReports from './pages/admin/reports'
import AdminSummaries from './pages/admin/summaries'
import AdminUsers from './pages/admin/users'
import AdminCreateTask from './pages/admin/createTask'
import AdminUpdateTask from './pages/admin/changeTask'
import AdminCreateReport from './pages/admin/createReport'
import AdminUpdateReport from './pages/admin/changeReport'
import AdminCreateSummary from './pages/admin/createSummary'
import AdminUpdateSummary from './pages/admin/changeSummary'
import UserTask from './pages/user/tasks'
import UserReports from './pages/user/reports'
import UserSummaries from './pages/user/summaries'
import UserCreateSummary from './pages/user/createSummary'
import UserUpdateSummary from './pages/user/changeSummary'
import UserCreateReport from './pages/user/createReport'
import UserUpdateReport from './pages/user/changeReport'
import NoAccess from './pages/noaccess'
import Home from './pages/home'
import Group from './pages/group'

export default class IRouter extends Component {

    constructor(props) {
        super(props)
        this.state = {
            isAdmin: sessionStorage.getItem("role") === 'admin'
        }
    }

    render() {
        const {isAdmin} = this.state
        return (
            <Router>
                <App>
                    <Route path='/login' component={Login}/>
                    <Route path='/' render={() =>
                        <Admin>
                            <Switch>
                                <PrivateRoute path='/admin/tasks' component={isAdmin ? AdminTask : NoAccess}/>
                                <PrivateRoute path='/admin/reports' component={isAdmin ? AdminReports : NoAccess}/>
                                <PrivateRoute path='/admin/summaries'
                                              component={isAdmin ? AdminSummaries : NoAccess}/>
                                <PrivateRoute path='/admin/users' component={isAdmin ? AdminUsers : NoAccess}/>
                                <PrivateRoute path='/admin/createTask'
                                              component={isAdmin ? AdminCreateTask : NoAccess}/>
                                <PrivateRoute path='/admin/updateTask/:task_id'
                                              component={isAdmin ? AdminUpdateTask : NoAccess}/>
                                <PrivateRoute path='/admin/createReport'
                                              component={isAdmin ? AdminCreateReport : NoAccess}/>
                                <PrivateRoute path='/admin/updateReport/:report_id'
                                              component={isAdmin ? AdminUpdateReport : NoAccess}/>
                                <PrivateRoute path='/admin/createSummary'
                                              component={isAdmin ? AdminCreateSummary : NoAccess}/>
                                <PrivateRoute path='/admin/updateSummary/:summary_id'
                                              component={isAdmin ? AdminUpdateSummary : NoAccess}/>
                                <PrivateRoute path='/user/tasks' component={UserTask}/>
                                <PrivateRoute path='/user/reports' component={UserReports}/>
                                <PrivateRoute path='/user/summaries' component={UserSummaries}/>
                                <PrivateRoute path='/user/createReport' component={UserCreateReport}/>
                                <PrivateRoute path='/user/updateReport/:report_id' component={UserUpdateReport}/>
                                <PrivateRoute path='/user/createSummary' component={UserCreateSummary}/>
                                <PrivateRoute path='/user/updateSummary/:summary_id'
                                              component={UserUpdateSummary}/>
                                <PrivateRoute path='/home'
                                              component={Group}/>
                                <PrivateRoute path='/group'
                                              component={Group}/>
                            </Switch>
                        </Admin>
                    }/>
                </App>
            </Router>
        )
    }
}

