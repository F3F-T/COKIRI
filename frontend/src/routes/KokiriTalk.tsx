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
    setProductImg, setTitle, setWishCategory, setTradeCategory, setTradeStatus
} from "../store/talkCardReducer";
import {use} from "js-joda";
import {log} from "util";
import timeConvert from "../utils/timeConvert";

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
const KokiriTalk = () => {
    const navigate = useNavigate();
    const scrollRef = useRef();
    const store = useSelector((state:Rootstate) => state);
    const [count,setCount] = useState(0)
    const [del,setDel] = useState(0)
    const [key,setKey] = useState<number>(null)
    const {state} = useLocation();
    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [roomId,setRoomId] = useState();
    const [contentInfo,setContentInfo]=useState(null)
    const [roomList,setRoomList]=useState(null)
    // const [count,setCount]=useState(0)
    console.log("count",count)
    const [input,setInput] = useState('');
    useEffect(()=>{
        getMessageRoom()
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
            console.log("실행된거니??",talkCard.id)
            setKey(talkCard.id)
            getMessageContent(talkCard.id);
        }
    },[talkCard.id])
    useEffect(() => {
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
            // try{
            //     const post_buyerId1 = {
            //         postId: talkCard.postId,
            //         buyerId: info.id
            //     }
            //     const res = await Api.post(`/post/${talkCard.postId}/messageRooms`,post_buyerId1);
            //     dispatch(setOpponetNick(res.data.sellerNickName))
            //     await dispatch(setMessageRoomId(res.data.id))
            //     await dispatch(setSellerId(res.data.sellerId))
            //     dispatch(setPostId(res.data.postId))
            //
            //     setRoomId(res.data.id)
            //     console.log("메세지룸 추가", res.data)
            //     // if(talkCard.id != undefined){
            //     //     sendMessage()
            //     // }
            //     alert("메세지룸 추가 성공")
            //     await sendMessage(res.data.id)
            // }
            // catch (err)
            // {
            //     console.log(err)
            //     alert("메세지룸 추가 실패")
            // }
            // console.log("제발좀ㅋㅋ아님",talkCard.id)
            await sendMessage(talkCard.id)
        }
        else{
            // console.log("제발좀ㅋㅋ",talkCard.id)
            await sendMessage(talkCard.id)
        }

    }
    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
            console.log("메세지룸 조회", res.data.content)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            // console.log("메세지룸 조회2",res2.data)
            if(talkCard.id === undefined){
                // const res3 = await Api.get(`/post/${res2.data[0].postId}`)
                for(let i=0;i<res2.data.length;i++){
                    if(res2.data[i].delStatus ==false){
                        if(res.data.content[0].messageRoomId == res2.data[i].id){
                            const res3 = await Api.get(`/post/${res2.data[i].postId}`)
                            dispatch(setTradeStatus(res3.data.tradeStatus))
                            dispatch(setTradeCategory(res3.data.tradeCategory))
                            dispatch(setWishCategory(res3.data.wishCategory))
                            dispatch(setProductImg(res3.data.images[0]))
                            dispatch(setTitle(res3.data.title))
                            dispatch(setPostId(res2.data[i].postId))
                            dispatch(setMessageRoomId(res2.data[i].id))
                            dispatch(setBuyerId(res2.data[i].buyerId))
                            if(info.id === res2.data[i].buyerId){
                                dispatch(setOpponetNick(res2.data[i].sellerNickName))
                            }
                            else{
                                dispatch(setOpponetNick(res2.data[i].buyerNickName))
                            }
                            getMessageContent(res2.data[0].id);

                        }
                    }

                }
                //title,wishCategory,productCategory,tradeStatu
            }

            setRoomList(()=>{
                return [...res.data.content]
            })
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
            // console.log("메세지룸 내용조회", res.data)
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
        try{
            const deleteInfo={
                data: {
                    id: talkCard.id,
                    memberId: info.id
                }
            }
            const res = await Api.delete(`/messageRooms/${talkCard.id}`,deleteInfo);

            // console.log("메세지룸 내용조회", res.data)
            alert("메세지룸 내용 삭제  in kokiritalk")

        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 내용 삭제 실패 in kokiritalk")
        }
    }


    if(!contentInfo){
        return null
    }
    if(!roomList){
        return null
    }


    return (
        <div className={styles.kokiritalk}>
            <div className={styles.left}>
                <div className={styles.leftHeader}>코끼리톡</div>
                <div className={styles.left2}>
                <div className={styles.talkContainer}>
                    {roomList.map((SingleObject:object) => (
                        SingleObject["buyerNickname"] === info.nickname ?
                        <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["sellerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                  onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>
                            :
                        <TalkList keys={SingleObject["messageRoomId"]} partner={SingleObject["buyerNickname"]} lastContent={SingleObject["lastMsg"]} date={timeConvert(SingleObject["createdDate"])}
                                  onClick = {onClickTotalTalkList(SingleObject["messageRoomId"])} counts={count}/>

                    ))}
                </div>
                </div>
            </div>

            <div className={styles.right}>
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
                <div ref={scrollRef} className = {styles.writeComments}>
                    <input type={"text"} className={styles.writeInput} placeholder={"쪽지를 보내세요"} value={input} onChange={onChangeMessage} />
                    <HiPencil className={styles.pencilIcon}  onClick={()=>{createMessageRoom(); setCount(prevState => prevState+1); setInput("") }}  />
                </div>
            </div>
        </div>
    );
}


export default KokiriTalk;