/**
 * 초기 값의 타입을 지정해주고,
 * action에 PayloadAction 제네릭 타입 추가를 해준다.
 *
 */

import {createSlice,PayloadAction} from "@reduxjs/toolkit";


/**
 * category버튼을 클릭했을때 state를 저장하는 slice
 */

interface InitialJwtTokenState{
    authenticated: boolean;
    accessToken : String;
    expireTime : Number;
}

const initialJwtTokenState : InitialJwtTokenState = {
    authenticated: false,
    accessToken : null,
    expireTime : null
}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const jwtTokenSlice = createSlice({
    name: "authToken",
    initialState : initialJwtTokenState,
    reducers : {
        setToken(state,action){
          state.authenticated = true;
          state.accessToken = action.payload.accessToken;
          state.expireTime = action.payload.expireTime;
        },
        deleteToken(state){
            state = initialJwtTokenState;
        }
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default jwtTokenSlice.reducer;
export const {setToken,deleteToken} = jwtTokenSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네