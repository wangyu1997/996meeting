import React, {Component} from 'react'
import {Card, Button, Table, message, Popconfirm} from 'antd'
import axios from '../../../network'
import Utils from '../../../utils/utils'
import BaseFilterForm from '../../../components/BaseForm'
import Schema from './schema'
import Api from '../../../network/Api.config'
import {withRouter} from 'react-router-dom'

class AdminTask extends Component {

    constructor(props) {
        super(props)
        this.state = {}
        this.params = {
            page: 1,
            pageSize: 10
        }
    }


    handleAddTask = () => {
        this.props.history.push('/admin/createTask')
    }


    componentDidMount() {
        this.requestList()
    }

    requestList = () => {
        axios.requestList(this, '/public/tasks/page', this.params)
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

    handleUpdate = () => {
        const selectRecord = this.state.selectRow
        if (selectRecord) {
            this.props.history.push(`/admin/updateTask/${selectRecord.id}`)
        }
    }

    onRowSelect = (record, index) => {
        this.setState({
            selectRow: record
        })
    }

    handleRowDelete = () => {
        const selectRecord = this.state.selectRow
        if (selectRecord) {
            axios.ajax({
                url: Api.Urls.adminDelTask + selectRecord.id,
                method: 'post'
            }).then((res) => {
                this.requestList()
                message.success("删除任务成功!")
            })
        }
    }


    render() {
        const op_item = {
            title: '操作',
            width:180,
            render: () => {
                return (
                    <span>
                        <Button type='primary' onClick={this.handleUpdate} style={{marginRight: 10}}>修改</Button>
                        <Popconfirm title="确定要删除这个任务吗？" onConfirm={this.handleRowDelete}>
                        <Button type='danger'>删除</Button>
                        </Popconfirm>
                    </span>
                )
            }
        }
        return <div>
            <Card className='card-wrap'>
                <BaseFilterForm formList={Utils.addKeyToFormList(Schema.formList)}
                                filterSubmit={(fieldsValue) => this.handleFilterSubmit(fieldsValue)}/>
            </Card>
            <Card>
                <Button type='primary' style={{marginRight: 10}} onClick={this.handleAddTask}>发布新任务</Button>
            </Card>
            <div className='content-wrap'>
                <Table
                    bordered
                    columns={[...Schema.columns, op_item]}
                    dataSource={this.state.list}
                    pagination={this.state.pagination}
                    onRow={(record, index) => {
                        return {
                            onMouseEnter: () => this.onRowSelect(record, index)
                        }
                    }}
                />
            </div>
        </div>
    }
}

export default withRouter(AdminTask)

