/**
 * 초기 값의 타입을 지정해주고,
 * action에 PayloadAction 제네릭 타입 추가를 해준다.
 *
 */

import {createSlice,PayloadAction} from "@reduxjs/toolkit";
import {create} from "domain";

interface InitialState{
    count : number
}

//initialState에서 결국 변수를 저장하고, 사용하는거구나

const initialState: InitialState = {count : 0}

//createSlice에선 action을 지정해준다. action 을 reducer라고 생각하면 되겠네.
//접근을 할때는 name으로 선언한
const counterSlice = createSlice({
    name: "counter",
    initialState : initialState,
    reducers : {
        increase(state){
            state.count+=1
        },
        decrease(state){
            state.count-=1
        },
        increaseByAmount(state, action : PayloadAction<number>){
            state.count += action.payload
        }
    }
})

/**
 * category버튼을 클릭했을때 state를 저장하는 slice
 */

interface InitialCategoryState{
    category : string
}

const initialCategoryState : InitialCategoryState = {category : "도서"}

const CategorySlice = createSlice({
    name: "category",
    initialState : initialCategoryState,
    reducers : {
        storeCategory(state){
            state.category = state.category;
        }
    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default counterSlice.reducer;
export const {increase,decrease,increaseByAmount} = counterSlice.actions;

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네