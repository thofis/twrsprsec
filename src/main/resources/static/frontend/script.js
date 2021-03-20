const loginButton = document.getElementById('btn_login');
const restCallButton = document.getElementById('btn_rest');
const username = document.getElementById('username');
const password = document.getElementById('password');

const rawJwt = document.getElementById('raw-jwt');
const jwtHeader = document.getElementById('jwt-header');
const jwtPayload = document.getElementById('jwt-payload');
const jwtSignature = document.getElementById('jwt-signature');

let isLoggedIn = false;

loginButton.addEventListener('click', login);
restCallButton.addEventListener('click', performRestCalls);




function login(event) {
    event.preventDefault();
    fetch('http://localhost:8080/login?' + new URLSearchParams({
            username: username.value,
            password: password.value
        }), {
        method: 'POST'
    }).then(response => {
        // for (var pair of response.headers.entries()) { // accessing the entries
        //     console.log(pair[0] + ' - ' + pair[1]);
        // }
        if (response.status === 200) {
            const jwt = extractJwtFromAuthorizationHeader(response);
            sessionStorage.setItem('jwt', jwt);
            populateLoginData(jwt);
            updateUIForLogin();
            isLoggedIn = true;
        }
    })
}

function logout(event) {
    sessionStorage.removeItem('jwt')
    updateUIForLogout();
    isLoggedIn = false;
}

function extractJwtFromAuthorizationHeader(response) {
    const authorizationHeader = response.headers.get('authorization');
    const jwt = authorizationHeader.split(" ")[1];

    return jwt;
}


function updateUIForLogout() {
    loginButton.innerText = 'Login';
    username.disabled = false;
    password.disabled = false;

    rawJwt.innerText = '';
    jwtHeader.innerText = '';
    jwtPayload.innerText = '';
    jwtSignature.innerText = '';

    loginButton.removeEventListener('click', logout);
    loginButton.addEventListener('click', login);
}

function updateUIForLogin() {
    loginButton.innerText = 'Logout';
    username.disabled = true;
    password.disabled = true;
    loginButton.removeEventListener('click', login);
    loginButton.addEventListener('click', logout);
}

function populateLoginData(jwt) {

    const jwtParts = jwt.split(".");

    const header = atob(jwtParts[0])
    const payload = atob(jwtParts[1]);
    const signature = jwtParts[2];

    rawJwt.innerText = jwt;
    jwtHeader.innerText = header;
    jwtPayload.innerText = payload;
    jwtSignature.innerText = signature;
}

function populateResponseInUI(response, id) {
    let element = document.getElementById(id);
    element.innerText =
        `Status: ${response.status} - ${getStatustext(response.status)}`;
    if (response.status >= 400) {
        element.classList.add('error');
    } else {
        element.classList.remove('error');
    }
}

function getStatustext(statuscode) {
    switch (statuscode) {
        case 200:
            return 'OK';
        case 201:
            return 'CREATED';
        case 204:
            return 'NO_CONTENT';
        case 400:
            return 'BAD REQUEST';
        case 401:
            return 'UNAUTHORIZED';
        case 403:
            return 'FORBIDDEN'
    }
}

function performRestCalls(event) {
    event.preventDefault();

    if (!isLoggedIn) {
        sessionStorage.removeItem('jwt');
    }

    performFetch('get', 'hello').then(response => populateResponseInUI(response, 'hello'));

    performFetch('get', 'articles', 123).then(response => populateResponseInUI(response, 'get-article'));
    performFetch('post', 'articles').then(response => populateResponseInUI(response, 'post-article'));
    performFetch('delete', 'articles', 123).then(response => populateResponseInUI(response, 'delete-article'));
    performFetch('put', 'articles', 123).then(response => populateResponseInUI(response, 'put-article'));

    performFetch('get', 'orders', 123).then(response => populateResponseInUI(response, 'get-order'));
    performFetch('post', 'orders').then(response => populateResponseInUI(response, 'post-order'));
    performFetch('delete', 'orders', 123).then(response => populateResponseInUI(response, 'delete-order'));
    performFetch('put', 'orders', 123).then(response => populateResponseInUI(response, 'put-order'));
}

async function performFetch(method, path, id) {
    const headers = new Headers();
    headers.append('authorization', `Bearer ${sessionStorage.getItem('jwt')}`)
    return await fetch(`http://localhost:8080/${path}${id ? `/${id}` : ''}`, {
        method: method,
        headers: headers
    });
}






