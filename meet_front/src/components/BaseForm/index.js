import React, {Component} from 'react'
import {Input, Select, Form, Button, Checkbox, DatePicker} from 'antd'
import Utils from '../../utils/utils'
import PropTypes from 'prop-types'
import zh_CN from 'antd/lib/locale-provider/zh_CN'
import 'moment/locale/zh-cn'

const FormItem = Form.Item
const {RangePicker} = DatePicker

class FilterForm extends Component {
    handleFilterSubmit = () => {
        let fieldsValue = this.props.form.getFieldsValue()
        this.props.filterSubmit(fieldsValue)
    }

    reset = () => {
        this.props.form.resetFields()
    }

    constructor(props) {
        super(props)
        this.state = {}
        this.initFormList()
    }

    componentDidMount() {
        this.initFormList()
    }

    initFormList = () => {
        const {getFieldDecorator} = this.props.form
        const formList = this.props.formList
        let formItemList = []
        if (formList && formList.length > 0) {
            formList.forEach((item) => {
                let label = item.label
                let field = item.field
                let initialValue = item.initialValue
                let placeholder = item.placeholder
                let width = item.width
                switch (item.type) {
                    case 'INPUT':
                        const INPUT = <FormItem label={label} key={field}>
                            {getFieldDecorator(field, {
                                initialValue: initialValue,
                            })(
                                <Input
                                    type='text'
                                    style={{width: width}}
                                    placeholder={placeholder}/>
                            )}
                        </FormItem>
                        formItemList.push(INPUT)
                        break
                    case 'SELECT':
                        const SELECT = <FormItem label={label} key={field}>
                            {getFieldDecorator(field, {
                                initialValue: initialValue
                            })(
                                <Select
                                    style={{width: width}}
                                    placeholder={placeholder}>
                                    {Utils.getOptionList(item.list)}
                                </Select>
                            )
                            }
                        </FormItem>
                        formItemList.push(SELECT)
                        break
                    case 'CHECKBOX':
                        const CHECKBOX = <FormItem label={label} key={field}>
                            {getFieldDecorator(field, {
                                valuePropName: 'checked',
                                initialValue: initialValue,
                            })(
                                <Checkbox
                                    style={{width: width}}
                                    placeholder={placeholder}>
                                    {label}
                                </Checkbox>
                            )}
                        </FormItem>
                        formItemList.push(CHECKBOX)
                        break
                    case 'TIME_QUERY':
                        const DATEPICKER = <FormItem label={label} key={field}>
                            {getFieldDecorator(field, {})(
                                <RangePicker
                                    locale={zh_CN}
                                    format='YYYY-MM-DD HH:mm'
                                    placeholder={['开始时间', '结束时间']}
                                    showTime/>
                            )}
                        </FormItem>
                        formItemList.push(DATEPICKER)
                        break
                    default:
                        break
                }
            })
        }
        return formItemList
    }

    render() {
        return <Form layout='inline'>
            {this.initFormList()}
            <FormItem style={{float:'right',marginTop:10}}>
                <Button type='primary' style={{margin: '0 20px'}} onClick={this.handleFilterSubmit}>查询</Button>
                <Button onClick={this.reset}>重置</Button>
            </FormItem>
        </Form>
    }
}

FilterForm.propTypes = {
    filterSubmit: PropTypes.func.isRequired,
    formList: PropTypes.array.isRequired
}

export default Form.create()(FilterForm)