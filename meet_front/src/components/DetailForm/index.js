import React, {Component} from 'react'
import {Input, Select, Form, Button, DatePicker, Upload, Icon} from 'antd'
import {Editor} from 'react-draft-wysiwyg'
import {EditorState} from 'draft-js'
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css'
import 'moment/locale/zh-cn'
import axios from "../../network"
import Api from "../../network/Api.config"
import PropTypes from 'prop-types'
import './index.less'

const FormItem = Form.Item
const TextArea = Input.TextArea
const Option = Select.Option
export default class DetailForm extends Component {
    constructor(props) {
        super(props)
        const {formParams} = this.props
        this.state = {
            requestList: [],
            formList: [],
            editorState: (formParams && formParams.description) || EditorState.createEmpty(),
            fileList: (formParams && formParams.file_ids && formParams.file_ids.fileList) || [],
            searchData: (formParams && formParams.searchData) || []
        }
    }

    componentDidMount() {
        this.initFormList()
    }

    handleSubmit = () => {
        this.props.form.validateFields((err, values) => {
            if (!err)
                this.props.handleValues(values)
        })
    }

    uploadImageCallBack = (file) => {
        let params = new FormData()
        params.append('file', file)
        return new Promise((resolve, reject) => {
            axios.ajax({
                url: '/user/upload/image',
                method: 'post',
                data: {
                    params,
                    isUpload: true
                },
                header: {'Content-type': 'multipart/form-data'}
            }).then((res) => {
                resolve({
                    data: {
                        link: res.url
                    }
                })
            }).then((err) => {
                reject(err)
            })
        })
    }


    onEditorStateChange = (editorState) => {
        this.setState({
            editorState
        })
    }

    initFormList = () => {
        const formItemLayout = {
            labelCol: {span: 3},
            wrapperCol: {span: 21}
        }
        const props = {
            action: `${Api.BaseUrl}${Api.Urls.uploadFile}`,
            headers: {
                'Access-Token': sessionStorage.getItem('access_token')
            },
            accept: 'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            onChange: ({file, fileList}) => {
                fileList = fileList.map((file) => {
                    if (file.response)
                        file.url = file.response.data.url
                    return file
                })
                fileList = fileList.filter((file) => {
                    if (file.response)
                        return file.response.success
                    return true
                })
                this.setState({fileList})
            },
            fileList: this.state.fileList,
            defaultFileList: []
        }
        const {getFieldDecorator} = this.props.form
        const formList = this.props.formList
        let formItemList = []
        if (formList && formList.length > 0) {
            formList.forEach((item) => {
                let label = item.label
                let field = item.field
                let initialValue = item.initialValue
                let placeholder = item.placeholder
                let rules = item.rules
                switch (item.type) {
                    case 'INPUT':
                        const INPUT = <FormItem key={field} label={label} {...formItemLayout}>
                            {getFieldDecorator(field, {
                                rules: rules,
                                initialValue: initialValue
                            })(
                                <Input placeholder={placeholder} type='text'/>
                            )}
                        </FormItem>
                        formItemList.push(INPUT)
                        break
                    case 'EDIT':
                        const EDIT = <FormItem key={field} label={label} {...formItemLayout}>
                            {getFieldDecorator(field, {
                                rules: rules,
                            })(
                                <Editor
                                    wrapperClassName="wysiwyg-wrapper"
                                    editorClassName="rich-editor"
                                    editorStyle={{lineHeight: '100%'}}
                                    editorState={this.state.editorState}
                                    onEditorStateChange={this.onEditorStateChange}
                                    toolbar={{
                                        inline: {inDropdown: true},
                                        list: {inDropdown: true},
                                        textAlign: {inDropdown: true},
                                        link: {inDropdown: true},
                                        history: {inDropdown: true},
                                        image: {
                                            urlEnabled: true,// 是否支持url形式引入
                                            uploadEnabled: true,// 是否支持上传
                                            previewImage: true,// 是否支持预览图片
                                            alignmentEnabled: true,// 是否显示排列按钮（相当于配置text-align）
                                            uploadCallback: this.uploadImageCallBack,// 上传的回调，这个是重点
                                            inputAccept: 'image/gif,image/jpeg,image/jpg,image/png,image/svg',
                                            alt: {present: true, mandatory: true},
                                        },
                                    }}
                                    placeholder={placeholder}
                                    spellCheck
                                    localization={{locale: 'zh', translations: {'generic.add': '添加'}}}
                                />
                            )}
                        </FormItem>
                        formItemList.push(EDIT)
                        break
                    case 'SELECT':
                        const mode = item.mode
                        const index = item.index
                        const searchData = this.state.searchData[index]
                        const SELECT = <FormItem key={field} label={label} {...formItemLayout}>
                            {getFieldDecorator(field, {
                                rules: rules,
                                initialValue: initialValue
                            })(
                                <Select
                                    mode={mode}
                                    filterOption={false}
                                    placeholder={placeholder}>
                                    {searchData && searchData.length > 0 ?
                                        searchData.map(tag => <Option key={tag.key}>{tag.label}</Option>) : ''
                                    }
                                </Select>)}
                        </FormItem>
                        formItemList.push(SELECT)
                        break
                    case 'TEXT_AREA':
                        const TEXT_AREA =
                            <FormItem key={field} label={label} {...formItemLayout}>{
                                getFieldDecorator(field, {
                                    rules: rules,
                                    initialValue: initialValue
                                })(<TextArea
                                    rows={5}
                                    placeholder={placeholder}/>)}
                            </FormItem>
                        formItemList.push(TEXT_AREA)
                        break
                    case 'DATE_PICKER':
                        const DATE_PICKER = <FormItem key={field} label={label} {...formItemLayout}>
                            {getFieldDecorator(field, {
                                rules: rules,
                                initialValue: initialValue
                            })(
                                <DatePicker
                                    showTime
                                    format='YYYY-MM-DD HH:mm'
                                    placeholder={placeholder}
                                />
                            )}
                        </FormItem>
                        formItemList.push(DATE_PICKER)
                        break
                    case 'UPLOAD':
                        const UPLOAD = <FormItem key={field} label={label} {...formItemLayout}>
                            {getFieldDecorator(field, {
                                rules: rules
                            })(
                                <Upload {...props}>
                                    <Button type='primary'>
                                        <Icon type="upload"/> Upload
                                    </Button>
                                </Upload>)}
                        </FormItem>
                        formItemList.push(UPLOAD)
                        break
                    default:
                        break
                }
            })
        }
        return formItemList
    }

    render() {
        const buttonLayout = {
            wrapperCol: {
                offset: 10,
                span: 4
            }
        }
        return <Form>
            {this.initFormList()}
            <FormItem {...buttonLayout}>
                <Button type='primary' style={{width: '100%'}} onClick={this.handleSubmit}>提交</Button>
            </FormItem>
        </Form>
    }
}

DetailForm.propTypes = {
    handleValues: PropTypes.func.isRequired,
    formList: PropTypes.array.isRequired,
    requestList: PropTypes.array.isRequired,
    formParams: PropTypes.object.isRequired
}

DetailForm.defaultProps = {
    requestList: [],
    formList: [],
    formParams: {}
}