<script setup lang="ts">
import { reactive ,ref, onMounted, watch, nextTick} from 'vue'
import ColorButton from '@/components/ColorButton.vue'
import type { ElInput } from 'element-plus'
import {reqCodeGeneration} from '@/api/generator'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import hljs from 'highlight.js/lib/core'
import python from 'highlight.js/lib/languages/python'
import 'highlight.js/styles/atom-one-dark.css' // 选择你喜欢的样式

// 注册语言
hljs.registerLanguage('python', python)

const codeBlock = ref()
const inputVisible = ref(false)
const inputAttrVisible = ref(false)
const inputValue = ref('')
const tagInputRef = ref<InstanceType<typeof ElInput>>()
const AttrInputRef = ref<InstanceType<typeof ElInput>>()
const genText = ref("请在左侧输入需求，点击‘生成’按钮...")


// 高亮函数调整
const highlightCode = () => {
  console.log('高亮函数执行...')
  nextTick(() => {
    setTimeout(() => {
      if (codeBlock.value) {
        // 清除原来的高亮标记
        delete codeBlock.value.dataset.highlighted
        // 关键操作：用 textContent 写入原始文本
        codeBlock.value.textContent = genText.value // ✅ 清空旧高亮
        hljs.highlightElement(codeBlock.value, {
          language: 'python',
          ignoreIllegals: true
        })
      }
    }, 0)
  })
}
// 初始高亮
onMounted(highlightCode)

watch(() => genText.value, ()=>{
  highlightCode()
} )

const copyCode = () => {
navigator.clipboard.writeText(genText.value)
}

// do not use same name with ref
const form = reactive({
  classDes:'',
  className:'',
  methods:[],
  attrs:[],
})

interface RuleForm {
  classDes: string
  className: string
  methods: string[]
  attrs: string[]
}

const formRef = ref<FormInstance>()


const onSubmit = async (formRef: any) => {
  // 步骤1：将 Element Plus 的 validate 方法 Promise 化
  const validateForm = () => new Promise((resolve, reject) => {
    formRef.validate((valid: boolean, invalidFields: any) => {
      valid ? resolve(true) : reject(invalidFields)
    })
  })

  try {
    // 步骤2：等待验证结果
    await validateForm()
    
    // 步骤3：验证通过后发起请求
    const result = await reqCodeGeneration(JSON.stringify(form))
    
    // 步骤4：处理响应数据
    if (result?.data) {
      genText.value = result.data
      ElMessage.success('生成成功')
    }
  } catch (error) {
    // 错误处理分支
    if (error instanceof Error) {
      // 网络请求错误
      ElMessage.error(`请求失败: ${error.message}`)
    } else {
      // 表单验证错误
      console.error('验证失败字段:', error)
      ElMessage.warning('请正确填写表单')
    }
  }
}

const onClear = ()=>{
  form.classDes=''
  form.className = ''
  form.methods = []
  form.attrs = []
}



// 删除标签
const handleTagClose = (index: number) => {
  form.methods.splice(index, 1)
}
const handleAttrClose = (index:number)=>{
  form.attrs.splice(index,1)
}

// 显示输入框
const showTagInput = () => {
  inputVisible.value = true
  nextTick(() => {
    tagInputRef.value?.input?.focus()
  })
}
const showAttrInput=()=>{
  inputAttrVisible.value = true
  nextTick(() => {
    AttrInputRef.value?.input?.focus()
  })
}

// 确认添加
const handleTagConfirm = () => {
  if (inputValue.value.trim()) {
    form.methods.push(inputValue.value.trim())
  }
  inputVisible.value = false
  inputValue.value = ''
}
const handleAttrConfirm= ()=>{
  if (inputValue.value.trim()) {
    form.attrs.push(inputValue.value.trim())
  }
  inputAttrVisible.value = false
  inputValue.value = ''
}
// 表单校验
const rules = reactive<FormRules<RuleForm>>({
  classDes:[
    { required: true, message: 'Please input class description', trigger: 'blur' }
  ],
  className:[
    { required: true, message: 'Please input class name', trigger: 'blur' }
  ],
})
</script>

<template>
  <div>
    <!-- <div class="nav">
      <h1>Welcome to this tool to generate Python class level code!</h1>
    </div> -->
    <ColorButton></ColorButton>
    <div class="content">
      <div class="input">
        <el-form :model="form" label-width="auto" style="max-width: 600px" label-position="left" class="inputForm" :rules="rules" ref="formRef">
          <el-form-item label="类功能描述" prop="classDes">
            <el-input v-model="form.classDes" />
          </el-form-item>
          <el-form-item label="类名" prop="className">
            <el-input v-model="form.className" />
          </el-form-item>
          <el-form-item label="方法列表">
            <!-- <el-input v-model="form.Methods" /> -->
             <!-- 已添加的标签 -->
            <el-tag
              v-for="(tag, index) in form.methods"
              :key="index"
              class="mx-1"
              closable
              @close="handleTagClose(index)"
            >
              {{ tag }}
            </el-tag>

            <!-- 动态输入框 -->
            <el-input
              v-if="inputVisible"
              ref="tagInputRef"
              v-model="inputValue"
              class="ml-1 w-20"
              size="small"
              @keyup.enter="handleTagConfirm"
              @blur="handleTagConfirm"
            />
            <el-button
              v-else
              class="button-new-tag ml-1"
              size="small"
              @click="showTagInput"
            >
              + 添加方法
            </el-button>
          </el-form-item>
          <el-form-item label="属性列表">
            <!-- <el-input v-model="form.Attrs" /> -->
            <el-tag
              v-for="(tag, index) in form.attrs"
              :key="index"
              class="mx-1"
              closable
              @close="handleAttrClose(index)"
            >
              {{ tag }}
            </el-tag>

            <!-- 动态输入框 -->
            <el-input
              v-if="inputAttrVisible"
              ref="AttrInputRef"
              v-model="inputValue"
              class="ml-1 w-20"
              size="small"
              @keyup.enter="handleAttrConfirm"
              @blur="handleAttrConfirm"
            />
            <el-button
              v-else
              class="button-new-tag ml-1"
              size="small"
              @click="showAttrInput"
            >
              + 添加属性
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit(formRef)">生成</el-button>
            <el-button @click="onClear">清空</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="output">
        <div class="result">
          <el-icon color="#409eff" class="copy-icon" @click="copyCode"><DocumentCopy /></el-icon>
          <pre><code class="language-python" ref="codeBlock">{{ genText }}</code></pre>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.content{
  margin-top: 10px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 20px;
  .input{
    border: 2px solid black;
    height: 400px;
    border-radius: 10px;
    margin-left: 10px;
    // background-color: #97ea87;
    .inputForm{
      margin: 70px auto;
      .mx-1 {
        margin-right: 0.25rem;
        margin-bottom: 0.25rem;
      }
      .ml-1 {
        margin-left: 0.25rem;
      }
      .w-20 {
        width: 100px;
      }
    }
  }
  .output{
    border: 2px solid black;
    height: 400px;
    border-radius: 10px;
    margin-right: 10px;
    // background-color: skyblue;
    .result{
      background-color: black;
      border-radius: 10px;
      color: white;
      width:90%;
      height: 90%;
      margin:20px auto;
      display:flow-root;
      position: relative;
      overflow: scroll;
      .copy-icon{
        position: absolute;
        right:5px;
        top:5px;
      }
      pre {
        background-color: #282c34;
        padding: 1em;
        border-radius: 4px;
        overflow-x: auto;
      }

      code {
        font-family: 'Fira Code', monospace;
        color: #abb2bf;
        font-size: 14px;
        line-height: 1.5;
      }

      /* 保留原始格式 */
      .language-python {
        white-space: pre;
        display: block;
      }
    }
  }
}
</style>