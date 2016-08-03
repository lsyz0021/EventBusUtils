# EventBusUtils
/***
 * 接收事件方法可以通过@Subscribe(priority = 1),priority的值来决定接收事件的顺序,
 * 数值越高优先级越大,默认优先级为0.(注意这里优先级设置只有在同一个线程模型才有效)
 */

/**
 * 不管从哪个线程发出的事件，MAIN模式都会在UI（主线程）线程执行onMessageEventMain()方法，
 * <p/>
 * 在这里可以更新UI的操作，不可以执行耗时的操作
 */
 @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    /**
 * 事件从哪个线程发布出来的，onMessageEventPost()方法就会在该线程中运行，
 * <p/>
 * 如果发送事件的线程是UI线程，则在UI线程执行，
 * 如果发送事件的线程是子线程，则在该子线程中执行
 */
@Subscribe(threadMode = ThreadMode.POSTING, priority = 2,sticky = true)

    /**
 * 如果发送事件的线程是UI线程，则重新创建新的子线程执行onMessageEventPost()方法，
 * 因此不能执行更新UI的操作
 * <p/>
 * 如果发送事件的线程是子线程，则在该子线程中执行onMessageEventPost()方法
 */
@Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 3)

    /**
 * 不管从哪个线程发出的事件，ASYNC模式都会创建一个新的子线程来执行onEventAsync()方法，
 * <p/>
 * 所以在这里不能执行更新UI的操作，可以执行耗时的操作
 */
@Subscribe(threadMode = ThreadMode.ASYNC, priority = 4)
    /***
 * 粘性事件，能够收到订阅之前发送的消息。但是它只能收到最新的一次消息，
 * 比如说在未订阅之前已经发送了多条黏性消息了，然后再订阅只能收到最近的一条消息。
 */

/***
 * 注意：
 *
 * @Subscribe 下的方法必须为public
 * postSticky()发送的粘性消息订阅时必须@Subscribe(sticky = true)否则接收不到
 * 发送的event事件是object类
 * @Subscribe(priority = 1) 使用时优先级默认为0，然后只有统一模式下设置优先级才有效果，自己看着合理使用
 *
 */
