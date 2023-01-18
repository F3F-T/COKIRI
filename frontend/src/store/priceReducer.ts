import {createSlice,PayloadAction} from "@reduxjs/toolkit";
import {stat} from "fs";

interface InitialpriceState {
    minPrice : string,
    maxPrice : string,
}

const initialPriceState : InitialpriceState = {
    minPrice: null,
    maxPrice: null,
}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const priceSlice = createSlice({
    name: "price",
    initialState : initialPriceState,
    reducers : {
        setPrice(state,action){
            state.minPrice = action.payload.minPrice;
            state.maxPrice= action.payload.maxPrice;
        },
        resetPrice(state){
            return initialPriceState
        }
    }
})

//위에서 선언해준 counterSlice의 reducer를 export해준다
export default priceSlice.reducer;
export const {setPrice,resetPrice} = priceSlice.actions;
//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네