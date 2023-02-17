import React, {useState, useEffect, useMemo, useCallback,useRef} from 'react';
import styles from "../styles/talk/kokiriTalk.module.scss"
import profileImg from "../img/profileImg.png"
import spamImg from "../img/spam.png"
import Comments from "../component/comments/Comments";
import Card from "../component/tradeCard/Card";
import TalkList from "../component/talk/TalkList";
import {useLocation, useNavigate} from "react-router-dom";
import Message from "../component/talk/Message";
import TalkCard from "../component/talk/TalkCard";
import {HiPencil} from "react-icons/hi";
import Api from "../utils/api";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../index";
import {
    setMessageRoomId,
    setOpponetNick,
    setPostId,
    setSellerId,
    setBuyerId,
    setProductImg, setTitle, setWishCategory, setTradeCategory, setTradeStatus, setDelStatus, setEdit,resetTalkCard
} from "../store/talkCardReducer";
import {use} from "js-joda";
import {log} from "util";
import timeConvert from "../utils/timeConvert";
import logOut from "./로그인 & 회원가입/Settings/LogOut";
import {update} from "list";

interface contentInfo {
        id: number,
        senderNickname: string,
        receiverNickname: string,
        content: string,
        senderId: string,
        receiverId: string,
        messageRoomId: number
}
interface contentInfo2 {
    id: number,
    senderNickname: string,
    receiverNickname: string,
    content: string,
    senderId: string,
    receiverId: string,
    messageRoomId: number
}
const usePreventLeave = () => {
    const listener = event => {
        event.preventDefault();
        event.returnValue = "";
    };
    const enablePrevent = () => window.addEventListener("beforeunload", listener); // beforeunload 이벤트 리스너로 listener 지정
    const disablePrevent = () =>
        window.removeEventListener("beforeunload", listener); // beforeunload 이벤트 제거
    return { enablePrevent, disablePrevent }; // 두 함수를 return
};
const KokiriTalk = () => {
    const navigate = useNavigate();

    const scrollRef = useRef();

    const store = useSelector((state:Rootstate) => state);
    const [count,setCount] = useState(0)
    const [click,setClick] = useState(0)

    const [del,setDel] = useState(0)
    const [key,setKey] = useState<number>(null)
    const {state} = useLocation();
    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [roomId,setRoomId] = useState();
    const [contentInfo,setContentInfo]=useState(null)
    const [roomList,setRoomList]=useState(null)
    const [roomList2,setRoomList2]=useState(null)
    const [deleteBtn,setDeleteBtn]=useState(false)


    const { enablePrevent, disablePrevent } = usePreventLeave();
    const [,updateState] = useState();

    // const [count,setCount]=useState(0)
    const [input,setInput] = useState('');
    console.log("초기시작으로 false를 해놧는데",talkCard.delStatus)
    useEffect(()=>{
        getMessageRoom()
        enablePrevent()
    },[])

    const onClickTotalTalkList = (key) => {
        return (event: React.MouseEvent) => {
            setKey(key);
            event.preventDefault();
        }
    }

    // console.log("talkCard??",talkCard.id)

    useEffect(()=>{
        if(talkCard.id!=undefined){
            // console.log("실행된거니??",talkCard.id)
            setKey(talkCard.id)
            getMessageContent(talkCard.id);
        }
    },[talkCard.id])
    useEffect(() => {
        getMessageRoom()
        getMessageRoom()

        // @ts-ignore
        // scrollRef.current.scrollIntoView({behavior:'smooth',block:'end',inline:'nearest'})
        // getMessageContent(talkCard.id)
    }, [count])

    useEffect(() => {
        getMessageRoom()
    }, [del])

    const onChangeMessage = (e) => {
        const inputMessage = e.target.value;
        setInput(inputMessage)
        return setInput
    }
    const createMessageRoom = async () => {

        //얘는 게시글통해서 들어왔을때만 그 톡방이 있는지 확인하면됨
        if(state===false){
            await sendMessage(talkCard.id)
        }
        else{
            // console.log("제발좀ㅋㅋ",talkCard.id)
            await sendMessage(talkCard.id)
        }

    }
    async function getMessageRoom() {
        try{
            console.log("삭제 그 후 2")

            const res = await Api.get('/user/messageRooms');
            console.log("count가 플러스가 되니까 이리로 넘어오겠지", count)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            console.log("메세지룸 조회",res.data.content)
            console.log("메세지룸 조회2",res2.data)

            for(let i=0;i<res2.data.length;i++){

            }
            if(talkCard.id === undefined){
                console.log("처음 켰을때",talkCard)
                // const res3 = await Api.get(`/post/${res2.data[0].postId}`)
                for(let i=0;i<res2.data.length;i++){
                    console.log("count가 플러스가 되니까 이리로 넘어오겠지2", count)
                    // 현재 유저가 buyer인지 seller인지 판단
                    if (info.id === res2.data[i].buyerId) {
                        if(res2.data[i].buyerDelStatus == false) {
                            //원래대로
                            console.log("buyerdelStatus가 false인 애들 중에 첫번째꺼를 앞에 띄워야지")
                            const res3 = await Api.get(`/post/${res2.data[i].postId}`)
                            dispatch(setTradeStatus(res3.data.tradeStatus))
                            dispatch(setTradeCategory(res3.data.tradeCategory))
                            dispatch(setWishCategory(res3.data.wishCategory))
                            dispatch(setProductImg(res3.data.images[0]))
                            dispatch(setTitle(res3.data.title))
                            dispatch(setPostId(res2.data[i].postId))
                            dispatch(setMessageRoomId(res2.data[i].id))
                            dispatch(setBuyerId(res2.data[i].buyerId))
                            getMessageContent(res2.data[i].id);
                            break;
                        }
                    } else {
                        if(res2.data[i].sellerDelStatus == false ) {
                            //원래대로 출력
                            console.log("SellerdelStatus가 false인 애들 중에 첫번째꺼를 앞에 띄워야지")
                            const res3 = await Api.get(`/post/${res2.data[i].postId}`)
                            dispatch(setTradeStatus(res3.data.tradeStatus))
                            dispatch(setTradeCategory(res3.data.tradeCategory))
                            dispatch(setWishCategory(res3.data.wishCategory))
                            dispatch(setProductImg(res3.data.images[0]))
                            dispatch(setTitle(res3.data.title))
                            dispatch(setPostId(res2.data[i].postId))
                            dispatch(setMessageRoomId(res2.data[i].id))
                            dispatch(setBuyerId(res2.data[i].buyerId))
                            getMessageContent(res2.data[i].id);
                            break;
                        }

                    }
                }
                //title,wishCategory,productCategory,tradeStatus
            }
            else{
                console.log("여기로온다면",talkCard)
            }

            for(let i=0;i<res2.data.length;i++){
                console.log("테스트입니다3",res.data.content[i])
                // 현재 유저가 buyer인지 seller인지 판단
                if (info.id === res2.data[i].buyerId) {
                    if(res2.data[i].buyerDelStatus == false ) {
                        console.log("테스트입니다1",res2.data[i].sellerNickName)
                        console.log("테스트입니다2",res2.data[i].buyerNickName)

                    }

                } else {
                    if(res2.data[i].sellerDelStatus == false ) {
                        //원래대로 출력
                    }
                }
            }

            console.log("여기는 처음킨게 아닐때 dispatch에 리스트 첫번째꺼를 넣어줄 필요가 없음")

            setRoomList(()=>{
                return [...res.data.content]
            })
            console.log("roomlist좀 보자",roomList)
            console.log("윤정데이터",roomList2)
            console.log("윤정데이터22",[res2.data[0]])


        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패 in kokiritalk")
        }
    }
    async function sendMessage(loading) {
        let messageInfo1={
            content:input,
            senderId:undefined,
            receiverId:undefined,
            postId:undefined,
            messageRoomId:undefined
        }
        try{
            if(info.id===talkCard.sellerId){
                console.log("메세지보내는사람이 파는 사람")
                messageInfo1={
                    content:input,
                    senderId:info.id,
                    receiverId:talkCard.buyerId,
                    postId:talkCard.postId,
                    messageRoomId:loading
                }
            }
            else{
                console.log("메세지보내는사람이 사는 사람")
                messageInfo1={
                    content:input,
                    senderId:info.id,
                    receiverId:talkCard.sellerId,
                    postId:talkCard.postId,
                    messageRoomId:loading
                }
            }
            // setInput("")
            const res = await Api.post(`/messageRooms/${loading}`,messageInfo1);
            console.log("메세지 전송", res.data)
        }
        catch (err)
        {
            console.log(err)
            alert("메세지전송  실패")
        }
    }
    async function getMessageContent(loading) {
        try{
            const res = await Api.get(`/messageRooms/${loading}`);
            console.log("메세지룸 내용조회", res.data)
            setContentInfo(()=>{
                return [...res.data]
            })
            return res.data
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 내용 조회 실패 in kokiritalk")
        }
    }

    async function deleteRoom() {
        console.log("메세지룸 내용조회 in delete22222")

        try{
            console.log("메세지룸 내용조회 in delete333333")

            const res2 = await Api.get(`/messageRooms/${talkCard.id}`);
            console.log("메세지룸 내용조회 in delete", res2.data.length)
            console.log('talkcard.id',talkCard.id)

            if(res2.data.length==0){
                try{
                    const deleteInfo={
                        data: {
                            id: talkCard.id,
                        }
                    }
                    const res = await Api.delete('/messageRooms',deleteInfo);

                    // console.log("메세지룸 내용조회", res.data)
                    alert("메세지룸 내용 영구삭제 ")
                    await dispatch(resetTalkCard())
                }
                catch (err)
                {
                    console.log(err)
                    alert("메세지룸 내용 영구삭제 실패")
                }
            }
            else{
                try{
                    const deleteInfo={
                        data: {
                            id: talkCard.id,
                            memberId: info.id,
                            postId: talkCard.postId
                        }
                    }
                    const res = await Api.delete(`/messageRooms/${talkCard.id}`,deleteInfo);

                    // console.log("메세지룸 내용조회", res.data)
                    alert("메세지룸 내용 삭제  in kokiritalk")

                    await dispatch(resetTalkCard())
                }
                catch (err)
                {
                    console.log(err)
                    alert("메세지룸 내용 삭제 실패 in kokiritalk")
                }


            }
            setDel(prevState => prevState+1);
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 내용 조회 실패 in delete")
        }

    }
    // if(!talkCard.delStatus){
    //     return null
    // }
    function scrollToBottom(){
        // console.log("내려가라고 시발라마 내려가라고 내려가라고")
        // @ts-ignore
        scrollRef.current.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' })
    }
    if(!contentInfo){
        return null
    }
    if(!roomList){
        return null
    }
    console.log("roomlist좀 보자22222",roomList2)
    return (
        <div className={styles.kokiritalk}>
            <div className={styles.left}>
                <div className={styles.leftHeader}>코끼리톡</div>
                <div className={styles.left2}>
                <div className={styles.talkContainer}>
                    {roomList.map((SingleObject:object) => (

                        SingleObject['messageRoomId'] === talkCard.id ?//눌렸냐
                            SingleObject["buyerNickname"] === info.nickname ?
                                // <>눌렸는데 구매자</>
                                SingleObject["buyerDelStatus"] === false?

                                    <div className={styles.wrapper}>
                                    <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["sellerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                              onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>
                                    </div>
                                    :
                                    <></>
                                :
                                // <>눌렸는데 판매자</>
                                SingleObject["sellerDelStatus"] === false?

                                    <div className={styles.wrapper}>
                                    <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["buyerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                              onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>
                                    </div>
                                    :
                                    <></>
                            :
                            // <>안눌림</>
                            SingleObject["buyerNickname"] === info.nickname ?
                                SingleObject["buyerDelStatus"] === false?
                                    <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["sellerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                              onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>
                                    :
                                    <></>
                                :
                                // <>눌렸는데 판매자</>
                                SingleObject["sellerDelStatus"] === false?

                                    <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["buyerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                              onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>
                                    :
                                    <></>

                    ))}
                </div>
                </div>
            </div>


            <div className={styles.right}>
                {
                    deleteBtn ==true?
                        <>
                            <div className={styles.right_headerBox}>
                                <div className={styles.right_header}>
                                    <div className={styles.right_header1}>
                                        <div className={styles.right_header1_1}> <TalkCard keys={key} /> </div>
                                    </div>
                                    <div className={styles.right_header2}>
                                        <button className={styles.sideBtn} onClick={()=>{deleteRoom();setDel(prevState => prevState+1);}} >삭제</button>
                                        <p> | </p>
                                        <button className={styles.sideBtn}>차단</button>
                                        <p> | </p>
                                        <button className={styles.sideBtn}>신고</button>
                                    </div>
                                </div>
                                <div className={styles.right_header1_2}>{talkCard.opponentNickname}님과의 쪽지방입니다.</div>
                            </div>
                            <div className={styles.talkContainer2}>
                                {key===null?
                                    <></>:<Message keys={key} counts={count}/>
                                }
                            </div>
                        </>
                        :
                        <>
                            <div className={styles.right_headerBox}>
                                <div className={styles.right_header}>
                                    <div className={styles.right_header1}>
                                        <div className={styles.right_header1_1}> <TalkCard keys={key} /> </div>
                                    </div>
                                    <div className={styles.right_header2}>
                                        <button className={styles.sideBtn} onClick={()=>{deleteRoom();}} >삭제</button>
                                        <p> | </p>
                                        <button className={styles.sideBtn}>차단</button>
                                        <p> | </p>
                                        <button className={styles.sideBtn}>신고</button>
                                    </div>
                                </div>
                                <div className={styles.right_header1_2}>{talkCard.opponentNickname}님과의 쪽지방입니다.</div>
                            </div>
                            <div className={styles.talkContainer2}>
                                {key===null?
                                    <></>:<Message keys={key} counts={count}/>
                                }
                            </div>
                        </>
                }

                <div ref={scrollRef} className = {styles.writeComments}>
                    <input type={"text"} className={styles.writeInput} placeholder={"쪽지를 보내세요"} value={input} onChange={onChangeMessage} />
                    <HiPencil className={styles.pencilIcon}  onClick={()=>{createMessageRoom(); setCount(prevState => prevState+1); setInput(""); }}  />
                </div>
            </div>
        </div>
    );
}


export default KokiriTalk;