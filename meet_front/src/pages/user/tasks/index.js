import React, {Component} from 'react'
import {Card, Table} from 'antd'
import axios from '../../../network'
import Utils from '../../../utils/utils'
import BaseFilterForm from '../../../components/BaseForm'
import Schema from './schema'
import {withRouter} from 'react-router-dom'

class UserTask extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.params = {
            page: 1,
            pageSize: 10
        }
    }


    componentDidMount() {
        this.requestList()
    }

    requestList = () => {
        axios.requestList(this, '/user/tasks/my', this.params)
    }

    handleFilterSubmit = (fieldsValue) => {
        this.params = Utils.concatFilter(this.params, fieldsValue)
        this.params.start_time_start = undefined
        this.params.start_time_end = undefined
        this.params.publish_time_start = undefined
        this.params.publish_time_end = undefined
        if (fieldsValue.time_query && fieldsValue.time_query.length > 0) {
            const time_query = fieldsValue.time_query
            this.params.start_time_start = time_query[0].format("YYYY-MM-DD HH:mm")
            this.params.start_time_end = time_query[1].format("YYYY-MM-DD HH:mm")
        }
        if (fieldsValue.publish_time_query && fieldsValue.publish_time_query.length > 0) {
            const publish_time_query = fieldsValue.publish_time_query
            this.params.publish_time_start = publish_time_query[0].format("YYYY-MM-DD HH:mm")
            this.params.publish_time_end = publish_time_query[1].format("YYYY-MM-DD HH:mm")
        }
        this.params.time_query = undefined
        this.params.publish_time_query = undefined
        this.requestList()
    }


    render() {
        return <div>
            <Card className='card-wrap'>
                <BaseFilterForm formList={Utils.addKeyToFormList(Schema.formList)}
                                filterSubmit={(fieldsValue) => this.handleFilterSubmit(fieldsValue)}/>
            </Card>
            <div className='content-wrap'>
                <Table
                    bordered
                    columns={Schema.columns}
                    dataSource={this.state.list}
                    pagination={this.state.pagination}
                />
            </div>
        </div>
    }
}

export default withRouter(UserTask)

