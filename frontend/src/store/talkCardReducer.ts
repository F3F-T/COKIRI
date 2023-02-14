/**
 * 초기 값의 타입을 지정해주고,
 * action에 PayloadAction 제네릭 타입 추가를 해준다.
 *
 */

import {createSlice,PayloadAction} from "@reduxjs/toolkit";
interface talkCardInfo {
    id:number,
    sellerId:number,
    buyerId:number,
    opponentNickname: string;
    productImg: string;
    tradeStatus: string;
    title:string;
    wishCategory?:string;
    tradeCategory:string;
    postId:number;
    delStatus:boolean;
}

const initialUserTalkCardInfoState : talkCardInfo = {
    id:undefined,
    sellerId: undefined,
    buyerId:undefined,
    opponentNickname: undefined,
    productImg: undefined,
    tradeStatus: undefined,
    title:undefined,
    wishCategory:undefined,
    tradeCategory:undefined,
    postId:undefined,
    delStatus:false
}

//state는 이 상태의 현재 상태 값을 의미한다
//action은 실제 페이지 컴포넌트에서 전달해준 값을 의미한다

const talkCardInfoSlice = createSlice({
    name: "talkCardInfo",
    initialState : initialUserTalkCardInfoState,
    reducers : {
        setReturnMessageRoomInfo(state,action){
            state.opponentNickname = action.payload.opponentNickname;
            state.productImg= action.payload.productImg;
            state.tradeStatus= action.payload.tradeStatus;
            state.title=action.payload.title;
            state.wishCategory=action.payload.wishCategory;
            state.tradeCategory=action.payload.tradeCategory;
        },
        setProductImg(state,action){
            state.productImg = action.payload;
        },
        setOpponetNick(state,action){
            state.opponentNickname = action.payload;
        },
        setTradeStatus(state,action){
            state.tradeStatus = action.payload;
        },
        setTitle(state,action){
            state.title = action.payload;
        },
        setWishCategory(state,action){
            state.wishCategory = action.payload;
        },
        setTradeCategory(state,action){
            state.tradeCategory = action.payload;
        },
        setMessageRoomId(state,action){
            state.id = action.payload;
        },
        setSellerId(state,action){
            state.sellerId = action.payload;
        },
        setBuyerId(state,action){
            state.buyerId = action.payload;
        },
        setPostId(state,action){
            state.postId = action.payload;
        },
        setDelStatus(state,action){
            state.delStatus = action.payload;
        },
        resetTalkCard(state){
            state.opponentNickname = undefined
            state.productImg = undefined
            state.tradeStatus=undefined
            state.title=undefined
            state.wishCategory=undefined
            state.tradeCategory=undefined
            state.id=undefined
            state.productImg=undefined
            state.sellerId=undefined
            state.buyerId=undefined
            state.postId=undefined
            state.delStatus = undefined;

        },

    }
})


//위에서 선언해준 counterSlice의 reducer를 export해준다
export default talkCardInfoSlice.reducer;
export const {
    setPostId,setSellerId,setBuyerId,setMessageRoomId,setProductImg,setOpponetNick,setTradeStatus,setTitle,setWishCategory,setTradeCategory,resetTalkCard,setDelStatus
} = talkCardInfoSlice.actions;

//이제 이걸다른 컴포턴트에서 dispatch로 사용한다.

//여기선 알아서 api가 해주는구나, 대충 여기서 선언한 initial state, type, action을 보내주면 된다고 생각하면 되겠네