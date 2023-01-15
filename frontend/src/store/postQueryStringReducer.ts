import {createSlice,PayloadAction} from "@reduxjs/toolkit";


interface InitialpostQueryStringState {

    category : string,
    minPrice : string,
    maxPrice : string,
}

const initialpostQueryStringState : InitialpostQueryStringState = {
    category: "",
    minPrice: "",
    maxPrice: "",
}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const postQueryStringSlice = createSlice({
    name: "postQueryString",
    initialState : initialpostQueryStringState,
    reducers : {
        setCategory(state,action){
            state.category = action.payload.category;
        },
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default postQueryStringSlice.reducer;
export const {setCategory} = postQueryStringSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네