### StickyDecorator

**Lightweight library to easy implement sectioning and sticky header.  
Some decoration use drawable, which gives great performance compared to view.**

![Sticky header](/images/sticky_header.gif)
![Sticky header reverse layout](/images/sticky_header_reverse.gif)
![Sticky view holder](/images/sticky_view_holder.gif)

**Important!**: this Drawable decorations tested only with LinearLayoutManager, with other it can work incorrectly

### Install
add repository in to root `build.gradle`
```
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

and add dependency   
```
implementation "ru.turlir.android:sticky-decoration:1.0.0"
```

### Usage    
#### ViewHolderStickyDecoration             
Easiest way to integrate sticky header is view holder based decoration [ViewHolderStickyDecoration](/sticky/src/main/java/com/osome/stickydecorator/ViewHolderStickyDecoration.java)
This decoration also support **vertical** GridLayoutManager(see example application)   
Firstly, you should make implement yours Adapter this interface `ViewHolderStickyDecoration.Condition` like this:
``` kotlin
class SectionItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewHolderStickyDecoration.Condition {

    override fun isHeader(position: Int): Boolean {
        return getItem(position) is SectionItem
    }
// other adapter's methods    
}
```
and after it add decoration in to RecyclerView
``` kotlin
recyclerView.addItemDecoration(ViewHolderStickyDecoration(recyclerView, adapter))
```
If you are use reverse layout
``` kotlin
recyclerView.addItemDecoration(ViewHolderStickyDecoration(recyclerView, adapter, true))
```

For GridLayoutManager you should set up SpanSizeLookup, for example
``` kotlin
val spanCount = 3
val lm = GridLayoutManager(recycler.context, spanCount)

lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        // here we detect header and make it full width by set up max span    
        if (adapter.getItemViewType(position) == SectionItemAdapter.TYPE_HEADER)
            return spanCount
        return 1 // or other value which are you want
    }
}
```
[Full example here](/app/src/main/java/com/osome/stickydecorator/SectionItemAdapter.kt)


#### VerticalDrawableSectionDecor
This decoration allows implement sectioning with drawable.        

**First step:**   
You should extends abstract class VerticalDrawableSectionDecor and implements two methods  
``` kotlin
class SectionDecor : VerticalDrawableSectionDecor() {
    override fun getDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        // here you return drawable which will be drawn
    }

    override fun getSectionHeight(position: Int): Int {
        // here you return section height.
        // this value uses for calculate drawable bounds for draw
    }
}
```
**Second step:**
Implement interface ConditionItemDecorator.Condition with single method to determine for which adapter item will be drawn section
``` kotlin
// here the first element and each 10th element will be split with section
val condition = ConditionItemDecorator.Condition { position ->
    (itemProvider.get(position).value + 1) % 10 == 0 || position == 0
}
```
**And finally**
``` kotlin
val decorator = ConditionItemDecorator(condition, SectionDecor())
recyclerView.addItemDecoration(decorator)                
```

also you can add top and bottom margins for section, just override two methods
``` kotlin
override fun getSectionMarginTop(): Int {
    // return value for top space
}

override fun getSectionMarginBottom(): Int {
    // return value for bottom space
}
```

In this case drawable will be drawn above item(for reverse layout too)   
Full examples [here](/app/src/main/java/com/osome/stickydecorator/decor/SectionDecor.kt) and [here](/app/src/main/java/com/osome/stickydecorator/decor/SectionDecorReverse.kt)

#### VerticalStickyDrawableDecor
This decoration allows implement sectioning and sticky header with drawable.    

**First step:**
You should extends abstract class VerticalStickyDrawableDecor and implement three methods   
``` kotlin
class StickySectionDecor : VerticalStickyDrawableDecor() {

    override fun getSectionHeight(position: Int): Int {
        // here you return section height.
        // this value uses for calculate drawable bounds for draw
        // and by default will be used for header height 
    }

    override fun getHeaderDrawable(position: Int, headerBounds: Rect): Drawable {
        // here you return drawable which will be drawn for header
    }

    override fun getSectionDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        // here you return drawable which will be drawn for section
    }
}
```

or for reverse layout

``` kotlin
class StickySectionDecor : VerticalStickyDrawableDecor(true) { <====  here set in to constructor flag for reverse layout 

    override fun getSectionHeight(position: Int): Int {
        // here you return section height.
        // this value uses for calculate drawable bounds for draw
        // and by default will be used for header height 
    }

    override fun getHeaderDrawable(position: Int, headerBounds: Rect): Drawable {
        // here you return drawable which will be drawn for header
    }

    override fun getSectionDrawable(position: Int, sectionBounds: Rect, child: View): Drawable {
        // here you return drawable which will be drawn for section
    }
}
```

**Second step:**
Implement interface ConditionItemDecorator.Condition with single method to determine for which adapter item will be drawn section
``` kotlin
// here the first element and each 10th element will be split with section
val condition = ConditionItemDecorator.Condition { position ->
    (itemProvider.get(position).value + 1) % 10 == 0 || position == 0
}
```
**And finally**
``` kotlin
val decorator = ConditionItemDecorator(condition, StickySectionDecor())
recyclerView.addItemDecoration(decorator)                
```  

also you can add top and bottom margins for header, just override two methods 
``` kotlin
    override fun getHeaderMarginTop(): Int {
        // return header top space
    }

    override fun getHeaderMarginBottom(): Int {
        // return header bottom space
    }
``` 

#### SimpleTextDrawable
To simplify usage drawable decorator library contains also SimpleTextDrawable class.   
This class allow build styled drawable 
``` kotlin
SimpleTextDrawable.Builder()
                .setPaddingSymmetricDp(12, 4)
                .setBackgroundColor(Color.LTGRAY)
                .setTextGravity(SimpleTextDrawable.SimpleGravity.LEFT)
                .setTextColor(Color.BLACK)
                .setTextSizeDp(14)
                .setTypeface(customTypeFace)
                .build()
```
