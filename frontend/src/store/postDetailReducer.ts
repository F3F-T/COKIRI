/**
 * 초기 값의 타입을 지정해주고,
 * action에 PayloadAction 제네릭 타입 추가를 해준다.
 *
 */

import {createSlice,PayloadAction} from "@reduxjs/toolkit";
import React from "react";


/**
 * category버튼을 클릭했을때 state를 저장하는 slice
 */
type cardTypes = "forTrade" | "forMypage"

interface InitialPostDetailState{
    className?: cardTypes;
    onClick?: (e : React.MouseEvent<HTMLButtonElement,MouseEvent>) => any;
    postTitle: string;
    postContent?: string;
    like?: number;
    comment?: number;
    category : string;
}

const InitialPostDetailState : InitialPostDetailState = {postTitle:"코끼리",postContent:"의류", like:0,comment:0,category:"없음"}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const postDetailSlice = createSlice({
    name: "postDetails",
    initialState : InitialPostDetailState,
    reducers : {
        storePostDetail(state,action){
            state.postTitle = action.payload;
            state.postContent = action.payload;
            state.like = action.payload;
            state.comment = action.payload;
            state.category = action.payload;
        },
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default postDetailSlice.reducer;
export const {storePostDetail} = postDetailSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네