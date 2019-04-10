import React, {Component} from 'react'
import {withRouter} from 'react-router-dom'
import {message} from "antd"

class NoAccess extends Component {
    constructor(props) {
        super(props)
        this.state = {}
    }

    componentWillMount() {
        const {history} = this.props
        setTimeout(() => {
            history.push("/group")
        }, 0)
    }

    render() {
        return <div> {message.error("无权操作")}</div>

    }
}

export default withRouter(NoAccess)