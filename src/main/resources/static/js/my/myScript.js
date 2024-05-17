// 연락처 수정 //

const sellerHp1 = document.getElementsByName('sellerHp1')[0];
const sellerHp2 = document.getElementsByName('sellerHp2')[0];
const sellerHp3 = document.getElementsByName('sellerHp3')[0];
const sellerFax1 = document.getElementsByName('sellerFax1')[0];
const sellerFax2 = document.getElementsByName('sellerFax2')[0];
const sellerFax3 = document.getElementsByName('sellerFax3')[0];

const FaxError = document.getElementById('sellerFaxError');
const resultSellerName = document.getElementById('resultSellerName');
const btnChangeSellerName = document.getElementById('btnChangeSellerName');
const sellerNameCheck= document.getElementById('sellerName');
const resultSellerHp= document.getElementById('resultSellerHp');
const btnChangeSellerHp= document.getElementById('btnChangeSellerHp');
const sellerHpError= document.getElementById('sellerHpError');
const btnChangeSellerFax = document.getElementById('btnChangeSellerFax');

let isNameOk  = false;
let isFaxOk = false;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;
const reName  = /^[가-힣]{2,10}$/
let reFax = /^\d{3}-\d{3,4}-\d{4}$/;
let isPassOk  = false;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;

window.onload = function (){
//// my/info ////
    const userId = document.getElementById('userId').className;
    // 판매자 이름 유효성 검사
    if(sellerNameCheck){
        sellerNameCheck.addEventListener('blur', function (){

            const value = sellerNameCheck.value;
            // 정규 표현식
            if (!value.match(reName)) {
                resultSellerName.innerText = "유효하지 않은 이름형식입니다.";
                resultSellerName.style.color = "red";
                sellerNameCheck.style.border = "1px solid red";
                isNameOk = false;
                return; // 여기서 끝!
            } else {
                resultSellerName.innerText = "";
                sellerNameCheck.style.border = "1px solid green";
                isNameOk = true;
                return;
            }
        });
    }
    // 판매자 이름 수정
    if (btnChangeSellerName) {
        btnChangeSellerName.onclick = function (e) {

            if (btnChangeSellerName.className === 'change') {
                sellerNameCheck.value = "";
                sellerNameCheck.style.border = "1px solid #999";
                sellerNameCheck.style.border = "1px solid #999";
                sellerNameCheck.readOnly = false;
                sellerNameCheck.readOnly = false;
                btnChangeSellerName.classList.remove('change');
                btnChangeSellerName.classList.add('save');
            } else if (btnChangeSellerName.className === 'save'){
                if (sellerNameCheck.value.trim() === "") {
                    // 수정 칸이 비어 있으면 수정하지 않음
                    alert("수정할 이름을 입력하세요.");
                    return;
                }
                const userIdValue = document.getElementById('userId').className;
                const sellerNameValue = document.getElementById('sellerName').value;
                e.preventDefault();
                console.log(userIdValue);

                fetch(`/lotteon/my/updateSellerName`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        "userId": userIdValue,
                        "sellerName": sellerNameValue
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);
                        if (data === 1) {
                            alert(`수정 완료!`);
                            sellerNameCheck.readOnly = true;
                            sellerNameCheck.style.border = "0";
                            sellerNameCheck.classList.add('change');
                            sellerNameCheck.classList.remove('save');
                        } else {
                            alert(`오류가 발생했습니다.`);
                        }
                    })
                    .catch(err => console.log(err));
            }
        }
    }
    // 판매자 연락처 수정 및 유효성 검사
    if (btnChangeSellerHp) {
        btnChangeSellerHp.onclick = function () {

            const totalSellerHp = sellerHp1.value + "-" + sellerHp2.value + "-" + sellerHp3.value;
            const value = totalSellerHp;
            const type = "sellerHp"

            if (!value.match(reHp)) {
                sellerHpError.innerText = "유효하지 않은 전화번호입니다.";
                sellerHpError.style.color = "red";
                isHpOk = false;
                return; // 여기서 끝!
            }
            // input태그 활성화
            if (btnChangeSellerHp.className === 'change') {
                sellerHp1.value = "";
                sellerHp2.value = "";
                sellerHp3.value = "";
                sellerHp1.style.border = "1px solid #999";
                sellerHp2.style.border = "1px solid #999";
                sellerHp3.style.border = "1px solid #999";
                sellerHp1.readOnly = false;
                sellerHp2.readOnly = false;
                sellerHp3.readOnly = false;
                btnChangeSellerHp.classList.remove('change');
                btnChangeSellerHp.classList.add('save');
            } else if (btnChangeSellerHp.className === 'save') {
                let savedSellerHp1 = sellerHp1.value;
                let savedSellerHp2 = sellerHp2.value;
                let savedSellerHp3 = sellerHp3.value;
                let sellerHp = savedSellerHp1 + "-" + savedSellerHp2 + "-" + savedSellerHp3;
                console.log(sellerHp)
                console.log(userId)
                const jsonData = {
                    "userId": userId,
                    "sellerHp": sellerHp
                };
                // 2. 중복 체크 (DB)
                fetch(`/lotteon/member/checkUser/${type}/${value}`) // DB에서 중복체크하고 올 controller
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);

                        if (data.result > 0) {
                            // 중복일 경우
                            sellerHpError.innerText = "중복된 전화번호입니다.";
                            sellerHpError.style.color = "red";
                            isHpOk = false;
                        } else {
                            // 중복이 아닐경우
                            sellerHpError.innerText = "사용가능한 전화입니다.";
                            sellerHpError.style.color = "green";
                            isHpOk = true;

                            fetch("/lotteon/my/updateSellerHp", {
                                method: 'POST',
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify(jsonData)
                            })
                                .then(response => {
                                    if (response.ok) {
                                        sellerHp1.readOnly = true;
                                        sellerHp2.readOnly = true;
                                        sellerHp3.readOnly = true;
                                        sellerHp1.style.border = "0";
                                        sellerHp2.style.border = "0";
                                        sellerHp3.style.border = "0";
                                        btnChangeSellerHp.classList.add('change');
                                        btnChangeSellerHp.classList.remove('save');
                                        alert("수정 완료!");
                                    } else {
                                        alert("수정실패");
                                    }
                                    return response.json();
                                })
                                .then(data => {
                                })
                                .catch(err => console.log(err));
                        }
                    })
                    .catch(err => console.log(err));
            }
        }
    }


    const userIdValue = document.getElementById('userId');
    const leave = document.getElementById('btnWithdraw');
    const userRole = document.getElementById('userRole');
    leave.onclick = function () {
        const userId = userIdValue.className;

        let result = confirm("정말 탈퇴하시겠습니까?");

        if (result) {
            const jsonData = {
                "userId": userId,
                "userRole": "DELETE"
            }
            fetch("/lotteon/my/updateRole", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    alert("회원탈퇴가 완료되었습니다.")
                })
                .catch(err => console.log(err))
        } else {

        }
    }
}

// 회원 팩스번호 수정
function changeSellerFax() {

    const totalFax = sellerFax1.value + "-" + sellerFax2.value + "-" + sellerFax3.value;
    const value = totalFax;
    const type = "fax";


    if (!value.match(reFax)) {
        FaxError.innerText = "유효하지 않은 팩스번호입니다.";
        FaxError.style.color = "red";
        isFaxOk = false;
        return; // 여기서 끝!
    }
    // input태그 활성화
    if (btnChangeSellerFax.className === 'change') {
        sellerFax1.value = "";
        sellerFax2.value = "";
        sellerFax3.value = "";
        sellerFax1.style.border = "1px solid #999";
        sellerFax2.style.border = "1px solid #999";
        sellerFax3.style.border = "1px solid #999";
        sellerFax1.readOnly = false;
        sellerFax2.readOnly = false;
        sellerFax3.readOnly = false;
        btnChangeSellerFax.classList.remove('change');
        btnChangeSellerFax.classList.add('save');
    } else if (btnChangeSellerFax.className === 'save') {
        // 수정 연락처 저장
        let saveFax1 = sellerFax1.value;
        let saveFax2 = sellerFax2.value;
        let saveFax3 = sellerFax3.value;
        let fax = saveFax1 + "-" + saveFax2 + "-" + saveFax3;

        const jsonData = {
            "userId": userId,
            "fax": fax
        };
        // 2. 중복 체크 (DB)
        fetch(`/lotteon/member/checkUser/${type}/${value}`) // DB에서 중복체크하고 올 controller
            .then(response => response.json())
            .then(data => {
                console.log(data);

                if (data.result > 0) {
                    // 중복일 경우
                    FaxError.innerText = "중복된 팩스 번호입니다.";
                    FaxError.style.color = "red";
                    isHpOk = false;
                } else {
                    // 중복이 아닐경우
                    FaxError.innerText = "사용가능한 팩스 번호입니다.";
                    FaxError.style.color = "green";
                    isHpOk = true;

                    fetch("/lotteon/my/updateFax", {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(jsonData)
                    })
                        .then(response => {
                            if (response.ok) {
                                sellerFax1.readOnly = true;
                                sellerFax2.readOnly = true;
                                sellerFax3.readOnly = true;
                                sellerFax1.style.border = "0";
                                sellerFax2.style.border = "0";
                                sellerFax3.style.border = "0";
                                btnChangeSellerFax.classList.add('change');
                                btnChangeSellerFax.classList.remove('save');
                                alert('수정 완료!');
                            } else {
                                alert("실패했습니다.")
                            }
                            return response.json();
                        })
                        .then(data => {
                        })
                        .catch(err => console.log(err));
                }
            })
            .catch(err => console.log(err));
    }
}
// 회원 연락처 수정
function changeUserHp() {
    const hp1 = document.getElementsByName('hp1')[0];
    const hp2 = document.getElementsByName('hp2')[0];
    const hp3 = document.getElementsByName('hp3')[0];
    const btnChangeHp = document.getElementById('btnChangeHp');
    const hpError = document.getElementById('hpError');
    const userId = document.getElementById('userId').className;
    let isHpOk    = false;

    const totalHp = hp1.value + "-" + hp2.value + "-" + hp3.value;
    const value = totalHp;
    const type = "userHp";

    if (!value.match(reHp)) {
        hpError.innerText = "유효하지 않은 전화번호입니다.";
        hpError.style.color = "red";
        isHpOk = false;
        return; // 여기서 끝!
    }
    // input태그 활성화
    if (btnChangeHp.className === 'change') {
        hp1.value = "";
        hp2.value = "";
        hp3.value = "";
        hp1.style.border = "1px solid #999";
        hp2.style.border = "1px solid #999";
        hp3.style.border = "1px solid #999";
        hp1.readOnly = false;
        hp2.readOnly = false;
        hp3.readOnly = false;
        btnChangeHp.classList.remove('change');
        btnChangeHp.classList.add('save');

    } else if (btnChangeHp.className === 'save') {
        // 수정 연락처 저장
        let saveHp1 = hp1.value;
        let saveHp2 = hp2.value;
        let saveHp3 = hp3.value;
        let userHp = saveHp1 + "-" + saveHp2 + "-" + saveHp3;

        const jsonData = {
            "userId" : userId,
            "userHp" : userHp
        };

        // 2. 중복 체크 (DB)
        fetch(`/lotteon/member/checkUser/${type}/${value}`) // DB에서 중복체크하고 올 controller
            .then(response => response.json())
            .then(data => {
                console.log(data);

                if (data.result > 0) {
                    // 중복일 경우
                    hpError.innerText = "중복된 전화번호입니다.";
                    hpError.style.color = "red";
                    isHpOk = false;
                } else {
                    // 중복이 아닐경우
                    hpError.innerText = "사용가능한 전화입니다.";
                    hpError.style.color = "green";
                    isHpOk = true;

                    // 중복이 아닐 때 수정 연락처 저장 요청
                    fetch("/lotteon/my/updateHp", {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(jsonData)
                    })
                        .then(response => {
                            if (response.ok) {
                                hp1.readOnly = true;
                                hp2.readOnly = true;
                                hp3.readOnly = true;
                                hp1.style.border = "0";
                                hp2.style.border = "0";
                                hp3.style.border = "0";
                                btnChangeHp.classList.add('change');
                                btnChangeHp.classList.remove('save');
                                hpError.innerText = "";
                                alert("수정 완료!")
                            }
                            return response.json();
                        })
                        .then(data => {
                            // 저장 완료 후 처리할 내용
                        })
                        .catch(err => console.log(err));
                }
            })
            .catch(err => console.log(err));
    }
}
function changeUserPw(){
// 비밀번호 수정 //
    const btnChangePass = document.getElementById('btnChangePass');
    const changePw = document.getElementById('changePw');
    const resultPw = document.getElementById('resultPw');

    const value = changePw.value;
    const type = "userPw";
    // input태그 활성화
    if (btnChangePass.className === 'change') {
        changePw.style.display = "inline-block";
        changePw.style.border = "1px solid #999";
        btnChangePass.classList.remove('change');
        btnChangePass.classList.add('save');

    } else if (btnChangePass.className === 'save') {
        //1. 정규 표현식 통과
        if (!value.match(rePass)) {
            resultPw.innerText = "유효하지 않은 패스워드입니다.";
            resultPw.style.color = "red";
            changePw.style.border = "1px solid red";
            isPassOk = false;
            return; // 여기서 끝!
        } else {
            resultPw.innerText = "사용가능한 비밀번호입니다.";
            resultPw.style.color = "green";
            changePw.style.border = "1px solid green";
            isPassOk = true;
            // 수정 비밀번호 저장
            let savePw = changePw.value;
            console.log(savePw);
            const userId = document.getElementById('userId').className;
            const jsonData = {
                "userId" : userId,
                "userPw" : savePw
            }
            fetch("/lotteon/my/updatePw", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            })
                .then(response => {
                    if (response.ok) {
                        changePw.style.display = "none";
                        btnChangePass.classList.add('change');
                        btnChangePass.classList.remove('save');
                        alert("수정 완료!");
                        resultPw.style.display = "none";
                    }
                    return response.json();
                })
                .then(data => {
                })
                .catch(err => console.log(err))
            return; // 여기서 끝!
        }

    }
}
function changeEmailBtn() {
    // 이메일 수정 //
    const btnChangeEmail = document.getElementById('btnChangeEmail');
    const inputEmailCode = document.getElementById('inputEmailCode');
    const checkCodeLabel = document.getElementById('checkCodeLabel');
    const resultEmailCode = document.getElementById('resultEmailCode');
    const checkEmailCode = document.getElementById('checkEmailCode');
    const emailCodeSection = document.getElementById('emailCodeSection');
    const resultEmail = document.getElementById('resultEmail');
    const email1 = document.getElementsByName('email1')[0];
    const email2 = document.getElementsByName('email2')[0];
    const emailError = document.getElementById('emailError');
    // 이메일 유효성 검사
    let isEmailOk = false;
    const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;


    const emailTotal = email1.value + "@" + email2.value;
    const value = emailTotal;
    const type = "userEmail";
    // 정규표현석
    if (!value.match(reEmail)) {
        emailError.innerText = "유효하지 않은 이메일 형식입니다.";
        emailError.style.color = "red";
        isEmailOk = false;
        return; // 여기서 끝!
    }
    // input태그 활성화
    if (btnChangeEmail.className === 'change') {

        // 이메일 수정 가능하게 하는 로직
        //email1.value = "";
        //email2.value = "";
        email1.style.border = "1px solid #999";
        email2.style.border = "1px solid #999";
        email1.readOnly = false;
        email2.readOnly = false;
        btnChangeEmail.classList.remove('change');
        btnChangeEmail.classList.add('checkCode');
        btnChangeEmail.innerText = "인증번호받기";

    } else if (btnChangeEmail.className === 'checkCode') {
        // 새로 작성한 이메일 인증 코드 받는 로직
        emailCodeSection.style.display = "block";
        let saveEmail1 = email1.value;
        let saveEmail2 = email2.value;
        const value = saveEmail1 + "@" + saveEmail2;
        console.log(value);
        // fetch 로 인증코드 받는 로직
        fetch(`/lotteon/member/findIdEmailCheck/${value}`)
            .then(response => response.json())
            .then(data => {
                if (data.result === 0) {
                    // 이메일이 존재하는 경우
                    resultEmail.innerText = "인증코드가 전송되었습니다.";
                    resultEmail.style.color = "green";
                    inputEmailCode.style.border = "1px solid green";
                } else {
                    // 이메일이 존재하지 않는 경우
                    resultEmail.innerText = "존재하지않는 이메일입니다.";
                    resultEmail.style.color = "red";
                    inputEmailCode.style.border = "1px solid red";
                }
            })
            .catch(error => {
                console.log('Error', error);
            });
        checkEmailCode.onclick = function (e) {
            e.preventDefault();

            const inputCode = inputEmailCode.value;
            console.log(inputCode);

            fetch(`/lotteon/member/checkEmailCode/${inputCode}`)
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    if (data.result > 0) {
                        resultEmailCode.innerText = "인증번호가 일치하지 않습니다.";
                        resultEmailCode.style.color = "red";
                        inputEmailCode.style.border = "1px solid red";
                    } else {
                        resultEmailCode.innerText = "인증번호가 인증되었습니다.";
                        resultEmailCode.style.color = "green";
                        inputEmailCode.style.border = "1px solid green";
                        resultEmail.style.display = "none";
                        checkEmailCode.style.display = "none";

                        btnChangeEmail.classList.remove('checkCode');
                        btnChangeEmail.classList.add('save');
                        btnChangeEmail.innerText = "저장하기";
                    }
                })
                .catch(err => console.log(err))
        }
    } else if (btnChangeEmail.className === 'save') {

        // 2. 중복 체크 (DB)
        fetch(`/lotteon/member/checkUser/${type}/${value}`) // DB에서 중복체크하고 올 controller
            .then(response => response.json())
            .then(data => {
                console.log(data);

                if (data.result > 0) {
                    // 중복일 경우
                    emailError.innerText = "중복된 이메일입니다.";
                    emailError.style.color = "red";
                    isEmailOk = false;
                    alert("수정에 실패했습니다.")
                } else {
                    // 중복이 아닐경우
                    emailError.innerText = "사용가능한 이메일입니다.";
                    emailError.style.color = "green";
                    isEmailOk = false;
                    saveEmail();
                    emailError.innerText = "";
                }
            })
            .catch(err => console.log(err))
        function saveEmail() {
            // 이메일 인증 이후 수정 이메일 저장 하는 로직
            let saveEmail1 = email1.value;
            let saveEmail2 = email2.value;
            let userEmail = saveEmail1 + "@" + saveEmail2;
            console.log(userEmail);
            const userId = document.getElementById('userId').className;

            const jsonData = {
                "userId" : userId,
                "userEmail" : userEmail
            }
            fetch("/lotteon/my/updateEmail", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(jsonData)
            })
                .then(response => {
                    if (response.ok) {
                        email1.readOnly = true;
                        email2.readOnly = true;
                        email1.style.border = "0";
                        email2.style.border = "0";
                        btnChangeEmail.classList.add('change');
                        btnChangeEmail.classList.remove('save');
                        btnChangeEmail.innerText = "수정하기";
                        inputEmailCode.value = "";
                        emailCodeSection.style.display = "none";
                        alert("수정 완료!")
                    }
                    return response.json();
                })
                .then(data => {
                })
                .catch(err => console.log(err))
        }
    }
}

    function changeAddrBtn(e) {
        const changeAddr = document.getElementById('changeAddr');
        const doneChangeAddr = document.getElementById('doneChangeAddr');
        const userZip = document.getElementById('userZip');
        const userAddr1 = document.getElementById('userAddr1');
        const userAddr2 = document.getElementById('userAddr2');
        const btnZip = document.getElementById('btnZip');
        const userIdValue = document.getElementById('userId');

        btnZip.style.display = "block";
        changeAddr.style.display = "none";
        doneChangeAddr.style.display = "block";

        userZip.style.border = "1px solid #999";
        userAddr1.style.border = "1px solid #999";
        userAddr2.style.border = "1px solid #999";
        userZip.readOnly = false;
        userAddr1.readOnly = false;
        userAddr2.readOnly = false;
        userZip.value = "";
        userAddr1.value = "";
        userAddr2.value = "";
    }
    function findZip(e) {
        //e.preventDefault();
        postcode();
    }
    function doneChangeAddrBtn() {
        const changeAddr = document.getElementById('changeAddr');
        const doneChangeAddr = document.getElementById('doneChangeAddr');
        const userZip = document.getElementById('userZip');
        const userAddr1 = document.getElementById('userAddr1');
        const userAddr2 = document.getElementById('userAddr2');
//다음 주소 열기
        const btnZip = document.getElementById('btnZip');
        const userIdValue = document.getElementById('userId');

        const zip = userZip.value;
        const addr1 = userAddr1.value;
        const addr2 = userAddr2.value;
        const userId = userIdValue.className;

        if (doneChangeAddr.innerText.trim() === "") {
            // 수정 칸이 비어 있으면 수정하지 않음
            alert("수정할 이름을 입력하세요.");
            return;
        }
        const jsonData = {
            "userZip": zip,
            "userId": userId,
            "userAddr1": addr1,
            "userAddr2": addr2
        }

        fetch("/lotteon/my/updateAddr", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(jsonData)
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data != null) {
                    doneChangeAddr.style.display = "none";
                    changeAddr.style.display = "block";
                    btnZip.style.display = "none";
                    userZip.style.border = "0";
                    userAddr1.style.border = "0";
                    userAddr2.style.border = "0";
                    userZip.readOnly = true;
                    userAddr1.readOnly = true;
                    userAddr2.readOnly = true;
                    alert("수정 완료!")
                } else {
                    alert("수정에 실패했습니다.")
                }

            })
            .catch(err => console.log(err))
    }