import React, {Component} from 'react'
import {List, Avatar, Icon, Card, Row, Col, Timeline} from 'antd'
import {withRouter} from 'react-router-dom'
import './index.less'
import axios from "../../network"
import Utils from "../../utils/utils"
import Api from "../../network/Api.config"


class ReportDetail extends Component {

    constructor(props) {
        super(props)
        this.state = {
            report_detail: {},
            report_success: false,
            report_id: this.props.match.params.report_id,
        }
        this.params = {}
    }

    componentDidMount() {
        this.requestDetail()
    }

    requestDetail = () => {
        const {report_id} = this.state
        axios.ajax({
            url: Api.Urls.querySummaryById + report_id,
            method: 'get',
            data: {
                isShowLoading: true
            }
        }).then((res) => {
            this.setState({
                report_success: true,
                report_detail: res
            })
        })
    }

    getUrl = () => {
        const num = Utils.randomNum(1, 25)
        return `/gallery/${num}.png`
    }

    render() {
        const url = this.getUrl()
        const {report_detail, report_success} = this.state
        return <div className='group_detail_page'>
            {report_success ? <Card
                className='card-wrap'
                cover={<div className='header-cover' alt='head_cover'
                            style={{backgroundImage: `url(${ url}`}}/>}>
                <div className='avatar-container'>
                    <Avatar style={{marginRight: 10}} src={report_detail.user.avatar}/>
                </div>
                <div className='time-container'>
                    <Icon type='clock-circle' style={{marginRight: 10}}/>{Utils.formateDate(report_detail.publish_time)}
                    <Icon type='team'
                          style={{marginRight: 10, marginLeft: 10}}/>{Utils.formateDate(report_detail.start_time)}
                </div>
                <div className='title-container'>
                    {report_detail.title}
                </div>
                <div className='desc-container'>
                    <div dangerouslySetInnerHTML={{__html: report_detail.description}}/>
                </div>
                <div className='users-container'>
                    汇报人: {report_detail.user.username}
                </div>
            </Card> : ''}
        </div>
    }

}

export default withRouter(ReportDetail)