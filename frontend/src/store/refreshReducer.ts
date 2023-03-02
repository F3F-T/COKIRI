import {createSlice,PayloadAction} from "@reduxjs/toolkit";


interface InitialRefreshState{
    commentChange : number,
    postChange : number
}

const initialCategoryState : InitialRefreshState = {commentChange : 0, postChange : 0}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const RefreshSlice = createSlice({
    name: "category",
    initialState : initialCategoryState,
    reducers : {
        changeCommentRefreshState(state){
            state.commentChange = state.commentChange + 1 ;
        },
        changePostRefreshState(state){
            state.postChange = state.postChange + 1 ;
        },
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default RefreshSlice.reducer;
export const {changeCommentRefreshState,changePostRefreshState} = RefreshSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네