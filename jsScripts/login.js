const axios = require('axios')
const fse = require('fs-extra')
const path = require("path");
let credFile = path.join(__dirname, "credentials.json");

async function login() {

    const {data} = await axios.post('https://play.battlecode.org/auth/token/', {
        username: process.env['BATTLECODE_USERNAME'], password: process.env['BATTLECODE_PASSWORD']
    })
    // fse.writeJsonSync(credFile, data)
    return data;
}

module.exports = login
