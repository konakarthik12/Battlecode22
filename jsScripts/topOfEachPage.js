const axios = require('axios')
const {login, requestMatch, getTeamsOnPage} = require("./api");


async function main() {
    const {access} = await login();
    axios.defaults.baseURL = 'https://play.battlecode.org/api/0/';
    axios.defaults.headers.common = {'Authorization': `Bearer ${access}`}

    for (let i = 2; i <= 7; i++) {
        let team = (await getTeamsOnPage(i)).find(team=>team['auto_accept_unranked']);
        await requestMatch(team.id)
    }

}


main()