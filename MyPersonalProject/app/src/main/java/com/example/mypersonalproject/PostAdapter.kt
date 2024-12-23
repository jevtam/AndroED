import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalproject.databinding.ItemPostBinding
import com.example.mypersonalproject.models.Post

class PostsAdapter(
    private val onFavoriteClicked: (Post) -> Unit,
    private val onFetchPosts: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)

        holder.binding.favoriteButton.setOnClickListener {
            val updatedPost = post.copy(isFavorite = !post.isFavorite)
            onFavoriteClicked(updatedPost)
            notifyItemChanged(position)
        }
    }

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.title.text = post.title
            binding.body.text = post.body
            binding.favoriteButton.setImageResource(
                if (post.isFavorite) R.drawable.ic_star else R.drawable.ic_star_outline
            )
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}