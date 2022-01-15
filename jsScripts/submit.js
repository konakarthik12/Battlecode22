import {login, requestMatch} from "./api";


async function main() {
    const {access} = await login();
    axios.defaults.baseURL = 'https://play.battlecode.org/api/0/';
    axios.defaults.headers.common = {'Authorization': `Bearer ${access}`}

    const {data} = await axios.post('team?ordering=-score,name&search=&page=1')
    const teams = data.results.filter(team => team['auto_accept_unranked']);
    for (const team of teams) await requestMatch(team.id)

}



main()