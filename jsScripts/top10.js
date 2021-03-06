const axios = require('axios')
const {login, requestMatch, getTeamsOnPage} = require("./api");


async function main() {
    const {access} = await login();
    axios.defaults.baseURL = 'https://play.battlecode.org/api/0/';
    axios.defaults.headers.common = {'Authorization': `Bearer ${access}`}


    let allTeams = await getTeamsOnPage(1);
    const teams = allTeams.filter(team => team['auto_accept_unranked']);
    for (const team of teams) await requestMatch(team.id)
}


main()