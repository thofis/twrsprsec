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
        method: 'POST',
        headers: addXsrfHeader()
    }).then(response => {
        if (response.status === 200) {
            fetchCookie().then(jwt => {
                populateLoginData(jwt);
            })
            updateUIForLogin();
            isLoggedIn = true;
        }
    })
}

function logout(event) {
    const headers = new Headers();
    addXsrfHeader();
    fetch(`http://localhost:8080/logout`, {
        headers: addXsrfHeader()
    }).then(response => {
        if (response.status === 200) {
            updateUIForLogout();
            isLoggedIn = false;
        } else {
            console.log('Something went wrong during logout.')
        }
    })
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

async function performRestCalls(event) {
    event.preventDefault();

    performFetch('get', 'hello').then(response => populateResponseInUI(response, 'hello'));
    await sleep(100);
    performFetch('get', 'hello-admin').then(response => populateResponseInUI(response, 'hello-admin'));
    await sleep(100);
    performFetch('get', 'hello-user').then(response => populateResponseInUI(response, 'hello-user'));
    await sleep(100);
    performFetch('get', 'articles', 123).then(response => populateResponseInUI(response, 'get-article'));
    await sleep(100);
    performFetch('post', 'articles').then(response => populateResponseInUI(response, 'post-article'));
    await sleep(100);
    performFetch('delete', 'articles', 123).then(response => populateResponseInUI(response, 'delete-article'));
    await sleep(100);
    performFetch('put', 'articles', 123).then(response => populateResponseInUI(response, 'put-article'));
    await sleep(100);
    performFetch('get', 'orders', 123).then(response => populateResponseInUI(response, 'get-order'));
    await sleep(100);
    performFetch('post', 'orders').then(response => populateResponseInUI(response, 'post-order'));
    await sleep(100);
    performFetch('delete', 'orders', 123).then(response => populateResponseInUI(response, 'delete-order'));
    await sleep(100);
    performFetch('put', 'orders', 123).then(response => populateResponseInUI(response, 'put-order'));
}

async function performFetch(method, path, id) {

    return fetch(`http://localhost:8080/${path}${id ? `/${id}` : ''}`, {
        method: method,
        headers: addXsrfHeader()
    });
}

async function fetchCookie() {
    const response = await fetch(`http://localhost:8080/jwt-from-cookie`);
    return await response.text();
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function addXsrfHeader() {
    return {
        'X-XSRF-TOKEN': readCookie('XSRF-TOKEN')
    };
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}







