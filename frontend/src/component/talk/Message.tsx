import styles from "../../styles/talk/talkList.module.scss";
import React, {useEffect, useState} from "react";
import MyPage from "../MyPageSet";
import {HiPencil} from "react-icons/hi";
import Api from "../../utils/api";
import {useDispatch, useSelector} from "react-redux";
import {Rootstate} from "../../index";
import {setBuyerId, setMessageRoomId, setOpponetNick, setPostId, setSellerId} from "../../store/talkCardReducer";


interface keyProps {
    keys: number;
}


const Message = (key: keyProps) => {
    const [contentInfo,setContentInfo]=useState(null)
    const dispatch = useDispatch();
    const talkCard = useSelector((state : Rootstate)=>{return state.talkCardReducer})
    const info = useSelector((state : Rootstate)=>{return state.userInfoReducer})
    const [roomList,setRoomList]=useState(null)
    const realKey = key.keys;
    console.log("realkey들어오지마자임 이게",realKey)

    const message: string[] = ["보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마",  "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
    ]
    //

    const objectMessage = [{
        keys: 1,
        message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "보낸 사람 : 삼천원"
        ]
    },
        {
            keys: 2,
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },
        {
            keys: 3,
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",

            ]
        },


    ]
    useEffect(()=>{
        getMessageRoom()
    },[realKey])

    async function getMessageRoom() {
        try{
            const res = await Api.get('/user/messageRooms');
            console.log("메세지룸 조회", res.data.content)
            const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
            console.log("메세지룸 조회2",res2.data)
            // dispatch(setMessageRoomId(res.data.content.messageRoomId))

            for(let i=0;i<res2.data.length;i++){
                if(res2.data[i].id == realKey){
                    dispatch(setPostId(res2.data[i].postId))
                    dispatch(setMessageRoomId(res2.data[i].id))
                    dispatch(setBuyerId(res2.data[i].buyerId))
                    dispatch(setSellerId(res2.data[i].sellerId))
                    if(info.id === res2.data[i].buyerId){
                        dispatch(setOpponetNick(res2.data[i].sellerNickName))
                    }
                    else{
                        dispatch(setOpponetNick(res2.data[i].buyerNickName))
                    }
                    getMessageContent(res2.data[i].id );

                }
            }


            setRoomList(()=>{
                return [...res.data.content]
            })
            // alert("메세지룸 조회 성공 in message")
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 조회 실패 in message")
        }
    }
    async function getMessageContent(loading) {
        try{
            const res = await Api.get(`/messageRooms/${loading}`);
            console.log("메세지룸 내용조회 in message", res.data)
            setContentInfo(()=>{
                return [...res.data]
            })
            // for(let i=0;i<res.data.length;i++){
            //     contentInfo1[i]=res.data[i]
            // }
            // console.log("바로들어간다니까",contentInfo1)
            return res.data
        }
        catch (err)
        {
            console.log(err)
            alert("메세지룸 내용 조회 실패 in message")
        }
    }
    // const loading = await createMessageRoom();
    const hi = () => {
        console.log("contentInfo",contentInfo)
        // console.log("contentInfo1",contentInfo1[0].content)
        // contentInfo1.map((a,i)=>(
        //     console.log("contentInfo222",contentInfo1[i].senderId)
        // ))
    }

    // if(!contentInfo1){
    //     return null
    // }

    if(!contentInfo){
        return null
    }
    if(!roomList){
        return null
    }
    console.log("키키키킼",key.keys);
    console.log("message.tsx에서 띄운거야",contentInfo)

    // axios.get( url/chatroom/realKey=1)



    // <p className={styles.sendContent}>{singleMessage.slice(8,-1)}</p>
    return (
        <>
            {/*{objectMessage[realKey-1].message.map((singleMessage: string) => (*/}
            {/*    singleMessage.includes("보낸 사람") ?*/}
            {/*        <div className={styles.send}>*/}
            {/*            <div className={styles.sendTitle}>받은 쪽지</div>*/}
            {/*            <p className={styles.sendContent}>{singleMessage.substring(7,20)}</p>*/}
            {/*        </div> :*/}
            {/*        <div className={styles.receive}>*/}
            {/*            <div className={styles.receiveTitle}>보낸 쪽지</div>*/}
            {/*            <p className={styles.receiveContent}>{singleMessage.slice(7,20)}</p>*/}
            {/*        </div>*/}
            {/*))}*/}

            {/*{contentInfo[realKey].map((singleMessage: string) => (*/}
            {/*    singleMessage.includes("보낸 사람") ?*/}
            {/*        <div className={styles.send}>*/}
            {/*            <div className={styles.sendTitle}>받은 쪽지</div>*/}
            {/*            <p className={styles.sendContent}>{singleMessage.substring(7,20)}</p>*/}
            {/*        </div> :*/}
            {/*        <div className={styles.receive}>*/}
            {/*            <div className={styles.receiveTitle}>보낸 쪽지</div>*/}
            {/*            <p className={styles.receiveContent}>{singleMessage.slice(7,20)}</p>*/}
            {/*        </div>*/}
            {/*))}*/}
            {contentInfo.map((a,i)=>(
               contentInfo[i].senderId === info.id?
                    <div className={styles.receive}>
                        <div className={styles.receiveTitle}>보낸 쪽지</div>
                        <p className={styles.receiveContent}>{contentInfo[i].content}</p>
                    </div>
                    :
                    <div className={styles.send}>
                        <div className={styles.sendTitle}>받은 쪽지</div>
                        <p className={styles.sendContent}>{contentInfo[i].content}</p>
                    </div>
            ))
            }



        </>
    )
}
export default Message;