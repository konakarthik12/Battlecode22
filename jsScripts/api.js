const axios = require('axios')

async function login() {

    const {data} = await axios.post('https://play.battlecode.org/auth/token/', {
        username: process.env['BATTLECODE_USERNAME'], password: process.env['BATTLECODE_PASSWORD']
    })
    return data;
}

async function getTeamsOnPage(page) {
    const {data} = await axios.post(`team?ordering=-score,name&search=&page=${page}`)
    return data.results
}

async function requestMatch(id) {
    return axios.post('scrimmage/', {
        "red_team": 2738, "blue_team": id, "ranked": false
    });
}

module.exports = {login, requestMatch, getTeamsOnPage}