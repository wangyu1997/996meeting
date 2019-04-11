import React, {Component} from 'react'
import {Form, Button, Checkbox, Input, Icon, Card, Row, Col, Timeline, Avatar} from 'antd'
import {withRouter} from 'react-router-dom'
import './index.less'
import axios from "../../network"
import Utils from "../../utils/utils"


class Report extends Component {

    constructor(props) {
        super(props)
        this.state = {
            list: []
        }
        this.params = {}
    }

    componentDidMount() {
        this.requestList()
    }

    requestList = () => {
        const data = {
            isShowLoading: true
        }
        axios.ajax({
            url: '/public/per_meets',
            data,
            method: 'get'
        }).then((res) => {
            if (res) {
                this.setState({
                    list: res,
                })
            }
        })
    }



    formatCard = (list) => {
        return list.map((item, index) => {
            return (
                <Timeline.Item dot={<Avatar src={item.user.avatar}/>}>
                    <div className='pane-container'
                         onClick={() => {
                             this.props.history.push(`/user/report/detail/${item.id}`)
                         }}>
                        <div className='time-container'>
                            <Icon type='clock-circle' style={{marginRight: 10}}/>{Utils.formateDate(item.publish_time)}
                        </div>
                        <div className='title-container'>
                            {item.title}
                        </div>
                        <div className='desc-container'>
                            {Utils.showText(Utils.deleteHtmlTag(item.description), 200)}
                        </div>
                    </div>
                </Timeline.Item>
            )
        })
    }

    render() {
        const {list} = this.state
        return <div className='report_page'>
            <div className='report_wrap'>
                <Timeline>
                    {this.formatCard(list)}
                </Timeline>
            </div>
        </div>
    }

}

export default withRouter(Report)