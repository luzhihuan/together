const bodyParser = require("body-parser");
const express = require("express");
const cors = require("cors");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const mysql = require("mysql2");
const { messageConfig } = require("element-plus");

const app = express();
const PORT = 3000;
const SECRET_KEY = "7f8c2b8a3b8f5a5c6f7e8d9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c";
//随机生成的32位secret_key

//中间件
app.use(cors());
app.use(bodyParser.json());  

//数据库连接
const db = mysql.createConnection({
    // host: "8.138.88.141",
    // user: "xzj",
    // password: "12345",
    // database: "user_auth",

    host: "localhost",
    user: "root",
    password: "root",
    database: "user_auth",
});

db.connect((err)=>{
    if(err) {
        console.error("数据库连接失败",err);
        return;
    }
    console.log("数据库连接成功");
});

//登录接口
app.post ("/api/login",(req,res) => {
    const { user_id, password} = req.body;

    //查询用户
    const query = "SELECT * FROM users WHERE user_id = ?";
    db.query(query, [user_id], async (err,results) =>{
        if(err) {
            return res.status(500),json({message:"数据库查询错误"});
        }

        if (results.length === 0){
            return res.status(404).json({message:"用户不存在"});
        }

        const user = results[0];
        //const isPasswordValid = await compare(password,user.password);

        if(password != user.password){
            return res.status(401).json({message:"密码错误"});
        }

        //生成JWT
        const token = jwt.sign({user_id: user.user_id, level:user.level}, SECRET_KEY, {expiresIn:"1h"});

        res.json({message:"登陆成功",token});
    });
}); 

//启动服务
app.listen(PORT, () =>{
    console.log('服务器运行在 http://localhost: ' + PORT);
});