/**
 * 초기 값의 타입을 지정해주고,
 * action에 PayloadAction 제네릭 타입 추가를 해준다.
 *
 */

import {createSlice,PayloadAction} from "@reduxjs/toolkit";
//TODO: 로그인을 하면 유저 정보가 여기 담기게 되니까 이걸 이용해서 마이페이지를 구성하면 됨
interface UserInfo {
    id: Number;
    email: string;
    userName: string;
    birthDate: number;
    nickname: string;
    phoneNumber: string;
    loginType: string;
    imageUrl : string;
    scrapId : Number;
    address : object;
}

const initialUserInfoState : UserInfo = {
    address: undefined,
    birthDate: 0,
    email: "",
    id: 0,
    imageUrl: "https://cdn-icons-png.flaticon.com/128/7178/7178514.png",
    loginType: "",
    nickname: "",
    phoneNumber: "",
    scrapId: 0,
    userName: ""
}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const userInfoSlice = createSlice({
    name: "userInfo",
    initialState : initialUserInfoState,
    reducers : {
        setUserInfo(state,action){
            state.id = action.payload.id;
            state.email = action.payload.email;
            state.userName = action.payload.userName;
            state.birthDate = action.payload.birthDate;
            state.nickname = action.payload.nickname;
            state.phoneNumber = action.payload.phoneNumber;
            state.email = action.payload.email;
            state.loginType = action.payload.loginType;
            state.imageUrl = action.payload.imageUrl;
            state.scrapId = action.payload.scrapId;
            state.address = action.payload.address;
        },
        setUserNick(state,action){
            state.nickname = action.payload;
        },
        setUserProfile(state,action){
            state.imageUrl = action.payload;
        },
        setUserName(state,action){
            state.userName = action.payload;
        },
        deleteUserInfo(state){
            state = initialUserInfoState;
        }
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default userInfoSlice.reducer;
export const {setUserInfo,deleteUserInfo,setUserNick,setUserProfile,setUserName} = userInfoSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네